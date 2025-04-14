import api from '@/api/index.js';

export const getNoticesApi = (params) => {
  return api
    .get(`/notices`, { params: params })
    .then((success) => success.result);
};

export const getNoticeApi = (noticeId) => {
  return api.get(`/notice/${noticeId}`).then((success) => success.result);
};

export const getNoticeRecipientsApi = (noticeId, params) => {
  return api
    .get(`/notice/${noticeId}/recipients`, { params: params })
    .then((success) => success.result);
};
