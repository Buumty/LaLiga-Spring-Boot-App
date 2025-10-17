import React from 'react';

export default function Buttons({ login, logout }) {
  return (
    <div className="row">
      <div className="col-md-12 text-center" style={{ marginTop: '30px' }}>
        <button
          className="btn btn-primary"
          style={{ margin: '10px' }}
          onClick={login}
        >
          Login
        </button>
        <button
          className="btn btn-dark"
          style={{ margin: '10px' }}
          onClick={logout}
        >
          Logout
        </button>
      </div>
    </div>
  );
}
