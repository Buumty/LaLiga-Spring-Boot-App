import React from 'react'
import LoggedHeader from '../../components/VisualComponents/LoggedHeader/LoggedHeader'
import Footer from '../../components/VisualComponents/Footer/Footer'
import "./MainPage.css"
import { Link } from 'react-router-dom'

function MainPage() {

  return (
    <div className='main'>
      <LoggedHeader />
      <div className='panel'>
        <div className='element'>
          <Link to="/round"><p>Schedule</p></Link>
        </div>
        <div className='element'>
          <Link to="/players-stats"><p>Player Statistics</p></Link>
        </div>
        <div className='element'>
          <Link to="/table"><p>Table</p></Link>
        </div>
        <div className='element'>
          <Link to="/ai"><p>Ai Prediction</p></Link>
          </div>
      </div>
      <Footer />
    </div>
  )
}

export default MainPage