import React from 'react'
import "./LoggedHeader.css"
import Logo from '../Logo/Logo'
import {Link} from "react-router-dom"

function LoggedHeader() {
  return (
    <div>
        <header className='header'>
            <div className='logo'></div>
                <Link to="/main"><Logo /></Link>
            <div className='navbar'>
                <Link to="/round">Schedule</Link>
                <Link to="/players-stats">Player Statistics</Link>
                <Link to="/table">Table</Link>
                <Link to="/ai">Ai Prediction</Link>
            </div>
        </header>
        
    </div>
  )
}

export default LoggedHeader