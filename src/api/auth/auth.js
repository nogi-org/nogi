import api from '@/api/index.js';
import { handleJoin, handleUserInfo } from '@/api/apiResponse.js';

export const getGithubLoginURL = () => {
  return api
    .get('/github/auth-url')
    .then(res => res.data.result)
    .catch(error => handleUserInfo(error.response));
};

export const apiLogout = () => {
  return api
    .put('v1/auth/out')
    .then(res => res.data)
    .catch(error => handleJoin(error.response.data));
};
