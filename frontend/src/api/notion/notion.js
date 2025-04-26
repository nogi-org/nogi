import api from '@/api/index.js';

export const getConnectedNotionApi = () => {
  return api.get(`/v1/notion/connection-check`).then(success => success);
};

export const updateNotionPageIdAdminApi = () => {
  return api
    .put(`/v1/admin/notion/users/page-ids`)
    .then(success => success.result);
};

// todo: 개발필요
export const getUserNotionPages = params => {
  return api
    .get(`/notion/${params.userId}/${params.pageId}/page`)
    .then(success => success.result);
};
