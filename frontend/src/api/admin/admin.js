import api from '@/api/index.js';

export const updateNotionPageIdOfAllUser = () => {
  return api.put(`/admin/notion-page-id`).then((success) => success.result);
};

export const getUsersInfo = () => {
  return api.get(`/admin/user-info`).then((success) => success.result);
};

// todo: notion 쪽으로이동
export const getNotionUserPage = (params) => {
  return api
    .get(`/notion/${params.userId}/${params.pageId}/page`)
    .then((success) => success.result);
};
