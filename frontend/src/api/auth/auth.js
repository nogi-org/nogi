import api from '@/api/index.js';
import { handleLogout } from '@/api/apiResponse.js';

export const getGithubLoginURL = () => {
  return api.get('/github/auth-url').then(success => success);
};

export const apiLogout = () => {
  return api
    .put('/logout')
    .then(success => handleLogout(success))
    .catch(error => handleLogout(error));
};
