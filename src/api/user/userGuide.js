import api from '@/api/index.js';

export const getUserGuidesApi = () => {
  return api.get(`/guide/list`).then(success => success.result);
};
