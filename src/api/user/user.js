import api from '@/api/index.js';
import {
  handleUserInfo,
  handleValidationGithubRepository
} from '@/api/apiResponse.js';

export const getUserInfoApi = () => {
  return api.get(`/users`).then(success => success);
};

export const updateUserInfoApi = payload => {
  return api
    .patch(`/users`, payload)
    .then(success => handleUserInfo(success))
    .catch(error => handleUserInfo(error));
};

export const checkValidationGithubRepositoryApi = params => {
  return api
    .get(`/users/validate-repository-name`, { params: params })
    .then(success => handleValidationGithubRepository(success))
    .catch(error => handleValidationGithubRepository(error));
};

// todo: response 객체 변경
export const onManualNogiApi = () => {
  return api
    .post(`/users/manual-nogi`)
    .then(success => handleUserInfo(success))
    .catch(error => handleUserInfo(error));
};
