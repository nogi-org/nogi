import api from '@/api/index.js';
import { handlePostDelete, handlePostRegister } from '@/api/apiResponse.js';

export const getUserGuidesApi = () => {
  return api.get(`/guide/list`).then(res => res.data.result);
};

// export const apiGetPost = postId => {
//   return api.get(`/v1/post/${postId}`).then(res => res.data);
// };
//
// export const apiRegisterPost = payload => {
//   return api
//     .post(`/v1/post/create`, payload)
//     .then(res => handlePostRegister(res.data))
//     .catch(error => handlePostRegister(error.response.data));
// };
//
// export const apiDeletePost = postId => {
//   return api
//     .delete(`/v1/post/${postId}/delete`)
//     .then(res => handlePostDelete(res.data))
//     .catch(error => handlePostDelete(error.response.data));
// };
