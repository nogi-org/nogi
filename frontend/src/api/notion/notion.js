import api from '@/api/index.js';
import { handleNotionPage } from '@/api/apiResponse.js';

export const getConnectedNotionApi = () => {
  return api.get(`/v1/notion/connection-check`).then(success => success);
};

export const updateNotionPageIdAdminApi = payload => {
  return api
    .put(`/v1/admin/notion/users/${payload.userId}/pages/${payload.pageId}`)
    .then(success => success.result);
};

export const getNotionPagesAdminApi = payload => {
  const { userId, ...params } = payload;
  return api
    .get(`/v1/admin/notion/users/${userId}/pages`, { params })
    .then(success => success.result)
    .catch(error => handleNotionPage(error));
};

export const getNotionDetailPageAdminApi = payload => {
  return api
    .get(`/v1/admin/notion/users/${payload.userId}/pages/${payload.pageId}`)
    .then(success => success.result);
};

export const getNotionDatabaseAdminApi = userId => {
  return api
    .get(`/v1/admin/notion/users/${userId}/database`)
    .then(success => success.result)
    .catch(error => handleNotionPage(error));
};

export const createDatabaseProperty = payload => {
  return api
    .post(`/v1/admin/notion/database/property`, payload, {
      headers: {
        'Content-Type': 'application/json'
      }
    })
    .then(success => success.result)
    .catch(error => handleNotionPage(error));
};
