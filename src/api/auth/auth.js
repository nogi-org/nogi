import api from '@/api/index.js';
import { handleJoin } from '@/api/apiResponse.js';

export const apiLoginKakao = payload => {
  return api
    .post('v1/auth/join', payload)
    .then(res => res.data)
    .catch(error => handleJoin(error.response.data));
};

export const apiLogout = () => {
  return api
    .put('v1/auth/out')
    .then(res => res.data)
    .catch(error => handleJoin(error.response.data));
};
