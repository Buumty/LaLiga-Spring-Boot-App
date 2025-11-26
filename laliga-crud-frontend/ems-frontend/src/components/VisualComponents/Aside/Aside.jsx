import React from 'react'
import LoginButton from '../Buttons/LoginButton/LoginButton'
import "./Aside.css"

function Aside() {
  return (
    <div className='aside'>
        <h2>
            Step into the LaLiga
        </h2>
        <p>Log in and check every LaLiga information you need!</p>
        <span><LoginButton /></span>
    </div>
  )
}

export default Aside