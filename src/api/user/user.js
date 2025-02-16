import api from '@/api/index.js';
import { handleUserInfo } from '@/api/apiResponse.js';

export const apiUpdateUserInfo = payload => {
  return api
    .put('/v1/user', payload)
    .then(res => handleUserInfo(res.data))
    .catch(error => handleUserInfo(error.response.data));
};

export const apiGetUserInfo = externalId => {
  // TOOD: 핸들러바꾸기
  return api
    .get(`/v1/user/${externalId}`)
    .then(res => res.data)
    .catch(error => handleUserInfo(error.response.data));
};
