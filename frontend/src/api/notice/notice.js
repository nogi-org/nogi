import api from '@/api/index.js';

export const getNoticesApi = (params) => {
  console.log('params ', params);
  return api
    .get(`/notices`, { params: params })
    .then((success) => success.result);
};

export const getNoticeApi = (noticeId) => {
  return api.get(`/notice/${noticeId}`).then((success) => success.result);
};
