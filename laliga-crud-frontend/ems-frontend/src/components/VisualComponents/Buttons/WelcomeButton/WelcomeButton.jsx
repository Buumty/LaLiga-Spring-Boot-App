import React from 'react'
import {useNavigate} from "react-router-dom"
import "./WelcomeButton.css"

function WelcomeButton() {
  const navigate = useNavigate();

  const handleLoginClick = () => {
    navigate("/");
  };

  return (
    <div className='welcome-button' onClick={handleLoginClick}>Go back to Welcome Page</div>
  )
}

export default WelcomeButton