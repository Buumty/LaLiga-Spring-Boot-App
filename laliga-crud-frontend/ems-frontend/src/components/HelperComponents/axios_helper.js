import axios from 'axios';

const AUTH_TOKEN_KEY = 'auth_token';

export const getAuthToken = () => localStorage.getItem(AUTH_TOKEN_KEY);

export const setAuthHeader = (token) => {
  if (token) {
    localStorage.setItem(AUTH_TOKEN_KEY, token);
  } else {
    localStorage.removeItem(AUTH_TOKEN_KEY);
  }
};

axios.defaults.baseURL = 'http://46.224.100.218:8080';
axios.defaults.headers.post['Content-Type'] = 'application/json';

export const request = (method, url, {data, params} = {}) => {
  const token = getAuthToken();

  const isAuthUrl = url.startsWith('/api/v1/auth/');

  const headers =
    !isAuthUrl && token && token !== 'null'
      ? { Authorization: `Bearer ${token}` }
      : {};

  return axios({ method, url, data, params, headers });
};
