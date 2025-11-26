import { Routes, Route } from 'react-router-dom';
import WelcomePage from '../pages/WelcomePage/WelcomePage.jsx';
import LoginPage from '../pages/LoginPage/LoginPage.jsx';
import MainPage from '../pages/MainPage/MainPage.jsx';
import ProtectedRoute from '../components/HelperComponents/ProtectedRoute.jsx';
import RoundPage from '../pages/RoundPage/RoundPage.jsx';
import PlayerStatsPage from '../pages/PlayerStatsPage/PlayerStatsPage.jsx';
import AIPage from '../pages/AIPage/AIPage.jsx';
import TablePage from '../pages/TablePage/TablePage.jsx';
function App() {
  

  return (
    <Routes>
      <Route path='/' element={<WelcomePage />}></Route>
      <Route path='/login' element={<LoginPage />}></Route>
      <Route path='/main' element={<ProtectedRoute><MainPage /></ProtectedRoute>}></Route>
      <Route path='players-stats' element={<ProtectedRoute><PlayerStatsPage /></ProtectedRoute>}></Route>
      <Route path='/round' element={<ProtectedRoute><RoundPage /></ProtectedRoute>}></Route>
      <Route path='/ai' element={<ProtectedRoute><AIPage /></ProtectedRoute>}></Route>
      <Route path='/table' element={<ProtectedRoute><TablePage /></ProtectedRoute>}></Route>
    </Routes>
  )
}


export default App;
