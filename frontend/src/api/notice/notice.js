import api from '@/api/index.js';
import { handleNoticePublish } from '@/api/apiResponse.js';

export const getNoticesApi = params => {
  return api
    .get(`/v1/notices`, { params: params })
    .then(success => success.result);
};

export const getNoticeApi = noticeId => {
  return api.get(`/v1/notices/${noticeId}`).then(success => success.result);
};

export const getNoticeRecipientsAdminApi = (noticeId, params) => {
  return api
    .get(`/v1/admin/notices/${noticeId}/recipients`, { params: params })
    .then(success => success.result);
};

export const noticePublishAdminApi = payload => {
  return api
    .post(`/v1/admin/notices`, payload)
    .then(success => handleNoticePublish(success))
    .catch(error => handleNoticePublish(error));
};
