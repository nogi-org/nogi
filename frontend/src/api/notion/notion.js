import api from '@/api/index.js';
import { handleNotionDatabaseConnectionTest } from '@/api/apiResponse.js';

export const onDatabaseConnectTest = () => {
  return api
    .post(`/notion/connection-test`)
    .then((success) => handleNotionDatabaseConnectionTest(success))
    .catch((error) => handleNotionDatabaseConnectionTest(error));
};

export const getUserNotionPages = (params) => {
  return api
    .get(`/notion/${params.userId}/${params.pageId}/page`)
    .then((success) => success.result);
};

export const updateNotionPageIdOfAllUser = () => {
  return api.put(`/admin/notion-page-id`).then((success) => success.result);
};
