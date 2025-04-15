import api from '@/api/index.js';
import { handleNoticePublish } from '@/api/apiResponse.js';

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

export const noticePublishApi = (payload) => {
  return api
    .post(`/notice/publish`, payload)
    .then((success) => handleNoticePublish(success))
    .catch((error) => handleNoticePublish(error));
};
