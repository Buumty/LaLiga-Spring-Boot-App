import React from 'react'
import {useNavigate} from "react-router-dom"
import "./LoginButton.css"

function LoginButton() {
  const navigate = useNavigate();

  const handleLoginClick = () => {
    navigate("/login");
  };

  return (
    <div className='login-button' onClick={handleLoginClick}>Log in now</div>
  )
}

export default LoginButton