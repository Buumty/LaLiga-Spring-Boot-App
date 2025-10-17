import './App.css'
import AppContent from './components/AppContent.jsx';
import Header from './components/Header.jsx'

function App() {
  

  return (
    <>
      <div>
        <Header pageTitle="Frontend authenticated with JWT" />
        <div className='container-fluid'>
          <div className='row'>
            <div className='col'>
              <AppContent />
            </div>
          </div>
        </div>
      </div>
    </>
  )
}


export default App;
