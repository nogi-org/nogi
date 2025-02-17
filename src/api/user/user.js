import api from '@/api/index.js';
import { handleUserInfo } from '@/api/apiResponse.js';

export const apiGetUserInfo = () => {
  return api.get(`/users`).then(success => success);
};

export const apiRegisterUserInfo = payload => {
  return api
    .patch(`/users`, payload)
    .then(success => handleUserInfo(success))
    .catch(error => handleUserInfo(error));
};
