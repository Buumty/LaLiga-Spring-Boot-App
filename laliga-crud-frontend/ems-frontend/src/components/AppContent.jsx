import React, { useState } from 'react';
import { request, setAuthHeader } from './axios_helper';

import Buttons from './Buttons';
import LoginForm from './LoginForm';
import WelcomeContent from './WelcomeContent';
import ListPlayerComponent from './ListPlayerComponent';

export default function AppContent() {
  const [componentToShow, setComponentToShow] = useState('welcome');

  const login = () => setComponentToShow('login');

  const logout = () => {
    setAuthHeader(null);
    setComponentToShow('welcome');
  };

  const onLogin = (e, email, password) => {
    e.preventDefault();
    request('POST', '/api/v1/auth/authenticate', { email, password })
      .then((response) => {
        setAuthHeader(response.data.token);
        setComponentToShow('players');
      })
      .catch(() => {
        setAuthHeader(null);
        setComponentToShow('login');
      });
  };

  const onRegister = (e, firstname, lastname, email, password) => {
    e.preventDefault();
    request('POST', '/api/v1/auth/register', { firstname, lastname, email, password })
      .then((response) => {
        setAuthHeader(response.data.token);
        setComponentToShow('players');
      })
      .catch(() => {
        setAuthHeader(null);
        setComponentToShow('welcome');
      });
  };

  return (
    <>
      <Buttons login={login} logout={logout} />

      {componentToShow === 'welcome' && <WelcomeContent />}
      {componentToShow === 'login' && (
        <LoginForm onLogin={onLogin} onRegister={onRegister} />
      )}
      {componentToShow === 'players' && <ListPlayerComponent />}
    </>
  );
}
