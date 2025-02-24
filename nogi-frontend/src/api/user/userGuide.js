import api from '@/api/index.js';

export const getUserGuidesApi = () => {
  return api.get(`/guides`).then(success => success.result);
};
