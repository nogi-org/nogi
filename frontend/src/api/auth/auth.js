import api from '@/api/index.js';
import { handleLogout } from '@/api/apiResponse.js';

export const getGithubLoginURLApi = () => {
  return api.get('/v1/github/auth-url').then(success => success);
};

export const getNotionLoginURLApi = params => {
  return api
    .get('/v1/notion/auth-url', { params: params })
    .then(success => success);
};

export const apiLogoutApi = () => {
  return api
    .put('/v1/logout')
    .then(success => handleLogout(success))
    .catch(error => handleLogout(error));
};
