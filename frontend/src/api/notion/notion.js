import api from '@/api/index.js';
import { handleNotionDatabaseConnectionTest } from '@/api/apiResponse.js';

export const getConnectedNotionApi = () => {
  return api
    .post(`/v1/notion/connection-check`)
    .then(success => handleNotionDatabaseConnectionTest(success))
    .catch(error => handleNotionDatabaseConnectionTest(error));
};

export const updateNotionPageIdAdminApi = () => {
  return api
    .put(`/v1/admin/notion/users/page-ids`)
    .then(success => success.result);
};

// todo: 수정필요
export const getUserNotionPages = params => {
  return api
    .get(`/notion/${params.userId}/${params.pageId}/page`)
    .then(success => success.result);
};
