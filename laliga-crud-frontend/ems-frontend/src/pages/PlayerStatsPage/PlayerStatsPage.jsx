import React from 'react'
import ListPlayerComponent from '../../components/VisualComponents/ListPlayerComponent/ListPlayerComponent'
import Footer from '../../components/VisualComponents/Footer/Footer'
import "./PlayerStatsPage.css"
import LoggedHeader from '../../components/VisualComponents/LoggedHeader/LoggedHeader'

function PlayerStatsPage() {
  return (
    <div className='players-page'>
        <LoggedHeader />
        <div className='players-wrapper'><ListPlayerComponent /></div>
        <Footer />
    </div>
  )
}

export default PlayerStatsPage