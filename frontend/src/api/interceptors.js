import {
  ApiResponse,
  convertResponseFormat,
  handleCommonError
} from '@/api/apiResponse.js';
import router from '@/router/router.js';
import { useAuthStore } from '@/stores/authStore.js';
import { useNotifyStore } from '@/stores/notifyStore.js';
import { useSpinnerStore } from '@/stores/spinnerStore.js';

export function setInterceptors(instance) {
  //요청 인터셉터
  instance.interceptors.request.use(
    (config) => {
      return config;
    },
    (error) => {
      //요청 에러 시 수행 로직
      return Promise.reject(error);
    }
  );

  //응답 인터셉터
  instance.interceptors.response.use(
    (response) => {
      return convertResponseFormat(response.data);
    },
    (error) => {
      const response = convertResponseFormat(error.response.data);
      handleInterceptorCommonError(response);
      return Promise.reject(response);
    }
  );
  return instance;
}

// 공통 에러처리
async function handleInterceptorCommonError(response) {
  const isCommonError = handleCommonError(response);
  if (!isCommonError) {
    return;
  }

  const notifyStore = useNotifyStore();
  const spinnerStore = useSpinnerStore();

  switch (response.code) {
    case ApiResponse.USER_2: // 401
      await router.push({ name: 'home' });
      useAuthStore().deleteAuth();
      notifyStore.onActive(response);
      spinnerStore.off();
      break;
    case ApiResponse.USER_3: // 403
      await router.push({ name: 'home' });
      useAuthStore().deleteAuth();
      notifyStore.onActive(response);
      spinnerStore.off();
      break;
  }
}
