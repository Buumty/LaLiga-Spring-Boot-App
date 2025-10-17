import axios from 'axios';

const AUTH_TOKEN_KEY = 'auth_token';

// Pobranie tokenu z localStorage
export const getAuthToken = () => localStorage.getItem(AUTH_TOKEN_KEY);

// Ustawienie lub usunięcie tokenu
export const setAuthHeader = (token) => {
  if (token) {
    localStorage.setItem(AUTH_TOKEN_KEY, token);
  } else {
    localStorage.removeItem(AUTH_TOKEN_KEY);
  }
};

// Konfiguracja axios
axios.defaults.baseURL = 'http://localhost:8080';
axios.defaults.headers.post['Content-Type'] = 'application/json';

// Pomocnicza funkcja do wykonywania zapytań
export const request = (method, url, data) => {
  const token = getAuthToken();

  // NIE dodawaj nagłówka dla endpointów auth
  const isAuthUrl = url.startsWith('/api/v1/auth/');

  const headers =
    !isAuthUrl && token && token !== 'null'
      ? { Authorization: `Bearer ${token}` }
      : {};

  return axios({ method, url, data, headers });
};
