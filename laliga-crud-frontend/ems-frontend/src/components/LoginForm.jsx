import React, { useState } from 'react';
import classNames from 'classnames';

export default function LoginForm({ onLogin, onRegister }) {
  const [active, setActive] = useState('login');
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName]   = useState('');
  const [email, setEmail]         = useState('');
  const [password, setPassword]   = useState('');

  const onChangeHandler = (e) => {
    const { name, value } = e.target;
    if (name === 'firstName') setFirstName(value);
    if (name === 'lastName')  setLastName(value);
    if (name === 'email')     setEmail(value);
    if (name === 'password')  setPassword(value);
  };

  const onSubmitLogin = (e) => {
    e.preventDefault();
    onLogin(e, email, password);
  };

  const onSubmitRegister = (e) => {
    e.preventDefault();
    onRegister(e, firstName, lastName, email, password);
  };

  return (
    <div className="row justify-content-center">
      <div className="col-4">
        <ul className="nav nav-pills nav-justified mb-3" role="tablist">
          <li className="nav-item" role="presentation">
            <button
              className={classNames('nav-link', active === 'login' && 'active')}
              onClick={() => setActive('login')}
            >
              Login
            </button>
          </li>
          <li className="nav-item" role="presentation">
            <button
              className={classNames('nav-link', active === 'register' && 'active')}
              onClick={() => setActive('register')}
            >
              Register
            </button>
          </li>
        </ul>

        <div className="tab-content">
          {active === 'login' && (
            <div className="tab-pane fade show active">
              <form onSubmit={onSubmitLogin}>
                <div className="form-outline mb-4">
                  <input
                    type="email"
                    id="loginEmail"
                    name="email"
                    className="form-control"
                    onChange={onChangeHandler}
                  />
                  <label className="form-label" htmlFor="loginEmail">Email</label>
                </div>

                <div className="form-outline mb-4">
                  <input
                    type="password"
                    id="loginPassword"
                    name="password"
                    className="form-control"
                    onChange={onChangeHandler}
                  />
                  <label className="form-label" htmlFor="loginPassword">Password</label>
                </div>

                <button type="submit" className="btn btn-primary btn-block mb-4">
                  Sign in
                </button>
              </form>
            </div>
          )}

          {active === 'register' && (
            <div className="tab-pane fade show active">
              <form onSubmit={onSubmitRegister}>
                <div className="form-outline mb-4">
                  <input
                    type="text"
                    id="firstName"
                    name="firstName"
                    className="form-control"
                    onChange={onChangeHandler}
                  />
                  <label className="form-label" htmlFor="firstName">First name</label>
                </div>

                <div className="form-outline mb-4">
                  <input
                    type="text"
                    id="lastName"
                    name="lastName"
                    className="form-control"
                    onChange={onChangeHandler}
                  />
                  <label className="form-label" htmlFor="lastName">Last name</label>
                </div>

                <div className="form-outline mb-4">
                  <input
                    type="email"
                    id="email"
                    name="email"
                    className="form-control"
                    onChange={onChangeHandler}
                  />
                  <label className="form-label" htmlFor="email">Email</label>
                </div>

                <div className="form-outline mb-4">
                  <input
                    type="password"
                    id="registerPassword"
                    name="password"
                    className="form-control"
                    onChange={onChangeHandler}
                  />
                  <label className="form-label" htmlFor="registerPassword">Password</label>
                </div>

                <button type="submit" className="btn btn-primary btn-block mb-3">
                  Sign up
                </button>
              </form>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
