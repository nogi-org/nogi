import api from '@/api/index.js';

export const getUserGuidesApi = () => {
  return api.get(`/v1/guides`).then(success => success.result);
};
