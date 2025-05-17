import api from '@/api/index.js';

export const getConnectedGithubApi = () => {
  return api.get(`/v1/github/validate`).then(success => success.result);
};
