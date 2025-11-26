// import React, { useState } from 'react';
// import { request, setAuthHeader } from '../../components/axios_helper.js';

// import Buttons from '../../components/Buttons/Buttons.jsx';
// import LoginForm from '../../components/LoginForm.jsx';
// import ListPlayerComponent from '../../components/ListPlayerComponent.jsx';

// export default function AppContent() {
//   const [componentToShow, setComponentToShow] = useState('welcome');

//   const login = () => setComponentToShow('login');

//   const logout = () => {
//     setAuthHeader(null);
//     setComponentToShow('welcome');
//   };

//   const onLogin = (e, email, password) => {
//     e.preventDefault();
//     request('POST', '/api/v1/auth/authenticate', { email, password })
//       .then((response) => {
//         setAuthHeader(response.data.token);
//         setComponentToShow('players');
//       })
//       .catch(() => {
//         setAuthHeader(null);
//         setComponentToShow('login');
//       });
//   };

//   const onRegister = (e, firstname, lastname, email, password) => {
//     e.preventDefault();
//     request('POST', '/api/v1/auth/register', { firstname, lastname, email, password })
//       .then((response) => {
//         setAuthHeader(response.data.token);
//         setComponentToShow('players');
//       })
//       .catch(() => {
//         setAuthHeader(null);
//         setComponentToShow('welcome');
//       });
//   };

//   return (
//     <>
//       <Buttons login={login} logout={logout} />

//       {componentToShow === 'welcome' && <WelcomeContent />}
//       {componentToShow === 'login' && (
//         <LoginForm onLogin={onLogin} onRegister={onRegister} />
//       )}
//       {componentToShow === 'players' && <ListPlayerComponent />}
//     </>
//   );
// }

import React from 'react'
import Header from '../../components/VisualComponents/Header/Header'
import Footer from '../../components/VisualComponents/Footer/Footer'
import Aside from '../../components/VisualComponents/Aside/Aside'
import "./WelcomePage.css"

function WelcomePage() {
  return (
    <div className='welcome-page'>
      <Header />
      <Aside />
      <Footer />
    </div>
  )
}

export default WelcomePage