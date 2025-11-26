import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom';
import { request, setAuthHeader } from '../../../HelperComponents/axios_helper';
import "./LoginForm.css"

function LoginForm() {
  const [mode, setMode ] = useState("login");
  const [form, setForm] = useState({firstName: "", lastName: "", email:"", password:""});
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const onChange = e => setForm(prev => ({...prev, [e.target.name]: e.target.value}));

 const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);
    try {
      const url =
        mode === "login"
          ? "/api/v1/auth/authenticate"
          : "/api/v1/auth/register";
      const body =
        mode === "login"
          ? { email: form.email, password: form.password }
          : {
              firstname: form.firstName,
              lastname: form.lastName,
              email: form.email,
              password: form.password,
            };
      const res = await request("POST", url, {data: body});

      if (!res.data || !res.data.token) {
        throw new Error("Brak tokena w odpowiedzi serwera");
      }

      setAuthHeader(res.data.token);
      navigate("/main", { replace: true });
    } catch (err) {
      console.error("Błąd logowania/rejestracji", err);

      if (err.response) {
        if (err.response.status === 401 && mode === "login" ){
          setError("Błędny email lub hasło");
        } else if (err.response.status === 409 && mode === "register") {
          setError("Użytkownik z takim emailem już istnieje.");
        } else {
          setError("Wystapił bład po stronie serwera. Spróbuj ponownie.")
        }
      } else {
        setError("Brak połączenia z serwerem")
      }
    } finally {
      setLoading(false);
    }
  };
  return (
    <div className="auth-page">
      <div className="auth-card">
        <h2 className="auth-title">
          {mode === "login" ? "Welcome back" : "Create account"}
        </h2>

        {error && <div className="auth-error">{error}</div>}

        <form className="auth-form" onSubmit={handleSubmit}>
          {mode === "register" && (
            <>
              <div className="form-group">
                <label>First name</label>
                <input
                  type="text"
                  name="firstName"
                  value={form.firstName}
                  onChange={onChange}
                  required
                />
              </div>
              <div className="form-group">
                <label>Last name</label>
                <input
                  type="text"
                  name="lastName"
                  value={form.lastName}
                  onChange={onChange}
                  required
                />
              </div>
            </>
          )}

          <div className="form-group">
            <label>Your email</label>
            <input
              type="email"
              name="email"
              value={form.email}
              onChange={onChange}
              placeholder="Enter your email"
              required
            />
          </div>

          <div className="form-group">
            <label>Password</label>
            <input
              type="password"
              name="password"
              value={form.password}
              onChange={onChange}
              placeholder="Enter your password"
              required
            />
          </div>

          {mode === "login" && (
            <div className="form-extra">
              <a href="#" className="forgot-link">
                Forgot your password?
              </a>
            </div>
          )}

          <button type="submit" className="btn btn-primary" disabled={loading}>
            {loading
              ? "Loading..."
              : mode === "login"
              ? "Login"
              : "Sign up"}
          </button>
        </form>

        <div className="auth-footer">
          {mode === "login" ? (
            <>
              <span>Don’t have an account?</span>
              <button
                className="btn btn-outline"
                onClick={() => setMode("register")}
              >
                Sign up
              </button>
            </>
          ) : (
            <>
              <span>Already have an account?</span>
              <button
                className="btn btn-outline"
                onClick={() => setMode("login")}
              >
                Login
              </button>
            </>
          )}
        </div>
      </div>
    </div>
  )
}


export default LoginForm