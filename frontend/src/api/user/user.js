import api from '@/api/index.js';
import { handleUserInfo } from '@/api/apiResponse.js';

export const getMeApi = () => {
  return api.get(`/v1/users/me`).then(success => success);
};

export const updateMeApi = payload => {
  return api
    .patch(`/v1/users/me`, payload)
    .then(success => handleUserInfo(success))
    .catch(error => handleUserInfo(error));
};

export const getUsersAdminApi = () => {
  return api.get(`/v1/admin/users`).then(success => success.result);
};

// todo: response 객체 변경
export const onManualSyncApi = () => {
  return api
    .post(`/v1/sync`)
    .then(success => handleUserInfo(success))
    .catch(error => handleUserInfo(error));
};
