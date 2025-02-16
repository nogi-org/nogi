import { useAuthStore } from '@/stores/authStore.js';
import { apiGetUserInfo, apiRegisterUserInfo } from '@/api/user/user.js';
import { reactive } from 'vue';
import { useSpinnerStore } from '@/stores/spinnerStore.js';
import { useApiResponseModalStore } from '@/stores/apiResponseModalStore.js';

export class UserManager {
  #authStore = useAuthStore();
  #apiResponseModalStore = useApiResponseModalStore();
  #spinnerStore = useSpinnerStore();

  #info = reactive({
    notionAuthToken: '',
    notionDatabaseId: '',
    role: ''
  });

  async getUserInfo() {
    const auth = this.#authStore.getAuth();
    const response = await apiGetUserInfo(auth.value.userId);
    return response.result;
  }

  async updateUserInfo(userInfo) {
    this.#spinnerStore.on();
    const auth = this.#authStore.getAuth();
    // todo: validation check
    const response = await apiRegisterUserInfo({
      ...userInfo,
      userId: auth.value.userId,
      githubDefaultBranch: 'main',
      githubRepository: 'nogi_demo'
    });
    this.#spinnerStore.off();
    this.#apiResponseModalStore.onActive(response);
    // todo: 성공 시 auth 쪽 필수정보 입력 값을 true로 변경
  }

  checkInfoValidation(userInfo) {
    const validation = {
      isResult: true,
      result: {}
    };

    if (!userInfo.notionAuthToken || !userInfo.notionAuthToken.trim()) {
      validation.isResult = false;
      validation.result['notionAuthToken'] = '꼭 필요한 정보에요!';
    }

    if (!userInfo.notionDatabaseId || !userInfo.notionDatabaseId.trim()) {
      validation.isResult = false;
      validation.result['notionDatabaseId'] = '꼭 필요한 정보에요!';
    }

    return validation;
  }
}
