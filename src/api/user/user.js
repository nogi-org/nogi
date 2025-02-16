import api from '@/api/index.js';
import { handleUserInfo } from '@/api/apiResponse.js';

export const apiGetUserInfo = userId => {
  return api.get(`/users/${userId}`).then(success => success);
};

export const apiRegisterUserInfo = payload => {
  return api
    .patch(`/users/${payload.userId}`, payload)
    .then(success => handleUserInfo(success))
    .catch(error => handleUserInfo(error));
};
