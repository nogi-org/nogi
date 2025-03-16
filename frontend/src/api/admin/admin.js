import api from '@/api/index.js';

export const updateNotionPageIdOfAllUser = () => {
  return api.put(`/admin/notion-page-id`).then((success) => success.result);
};
