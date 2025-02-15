import { ApiResponse, handleCommonError } from '@/api/apiResponse.js';
import router from '@/router/index.js';
import { apiResponseModalStore } from '@/stores/modalStore.js';

export function setInterceptors(instance) {
  //요청 인터셉터
  instance.interceptors.request.use(
    config => {
      // todo: 쿠키로 바꾸면 필요없음
      // config.headers.Authorization = AuthManager.getToken();
      return config;
    },
    error => {
      //요청 에러 시 수행 로직
      return Promise.reject(error);
    }
  );

  //응답 인터셉터
  instance.interceptors.response.use(
    response => {
      //응답에 대한 로직
      return response;
    },
    error => {
      handleInterceptorCommonError(error);
      return Promise.reject(error);
    }
  );

  return instance;
}

// 공통 에러처리
function handleInterceptorCommonError(error) {
  const response = handleCommonError(error.response);
  if (!response) return;
  if (response.code === ApiResponse.AUTH_2) {
    router.push({ name: 'home' }).then(() => {
      // todo: 작업하기
      // authStore().deleteAuth();
      apiResponseModalStore().onActive(response);
    });
  } else {
    apiResponseModalStore().onActive(response);
  }
}
