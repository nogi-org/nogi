import { AuthManager } from '@/manager/auth/AuthManager.js';
import { ApiResponse, handleCommonError } from '@/api/apiResponse.js';
import router from '@/router/index.js';
import { apiResponseModalStore } from '@/stores/modalStore.js';
import { useAuthStatusStore } from '@/stores/authStore.js';

export function setInterceptors(instance) {
  // const authManager = new AuthManager();
  //요청 인터셉터
  instance.interceptors.request.use(
    config => {
      config.headers.Authorization = AuthManager.getToken();
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
  const response = handleCommonError(error.response.data);
  if (!response) return;
  if (response.code === ApiResponse.AUTH_2) {
    router.push({ name: 'home' }).then(() => {
      useAuthStatusStore().deleteAuth();
      apiResponseModalStore().onActive(response);
    });
  } else {
    apiResponseModalStore().onActive(response);
  }
}
