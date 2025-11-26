import { Navigate } from 'react-router-dom'
import { getAuthToken } from './axios_helper'

export default function ProtectedRoute({ children }) {
  const token = getAuthToken();
  if (!token) return <Navigate to="/login" replace />;
  return children;
}