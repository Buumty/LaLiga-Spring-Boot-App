import React from 'react'
import RoundComponent from '../../components/VisualComponents/RoundComponent/RoundComponent'
import Footer from '../../components/VisualComponents/Footer/Footer'
import './RoundPage.css'
import LoggedHeader from '../../components/VisualComponents/LoggedHeader/LoggedHeader'

function RoundPage() {
  return (
     <div className="page-wrapper">
      
      {/* Header full width */}
      <LoggedHeader />

      {/* Main content with background */}
      <div className="round-page">
        <div className="round-component">
          <RoundComponent />
        </div>
      </div>

      {/* Footer full width */}
      <Footer />
    </div>
  )
}

export default RoundPage