import { useAuthStore } from '@/stores/authStore.js';
import {
  checkValidationGithubRepositoryApi,
  getConnectedGithubInfoApi,
  getUserInfoApi,
  onManualNogiApi,
  updateUserInfoApi
} from '@/api/user/user.js';
import { ref } from 'vue';
import { useSpinnerStore } from '@/stores/spinnerStore.js';
import { useNotifyStore } from '@/stores/notifyStore.js';

export class UserManager {
  #authStore = useAuthStore();
  #apiResponseModalStore = useNotifyStore();
  #spinnerStore = useSpinnerStore();

  #info = ref({});
  #githubInfo = ref({});
  #infoUpdateValidation = ref({
    isSuccess: false,
    result: {}
  });
  #isSuccessRepositoryNameCheck = ref(false);
  #isSuccessNotionDatabaseConnectionTest = ref(false);

  async getConnectedGithubInfo() {
    this.#spinnerStore.on();
    this.#githubInfo.value = await getConnectedGithubInfoApi();
    this.#spinnerStore.off();
    console.log('response : ', this.#githubInfo.value);
  }

  async getInfo() {
    this.#spinnerStore.on();
    const response = await getUserInfoApi();
    this.#spinnerStore.off();
    this.#info.value = response.result;
    this.#isSuccessNotionDatabaseConnectionTest.value =
      this.#info.value.notionDatabaseId && this.#info.value.notionBotToken;
    this.#isSuccessRepositoryNameCheck.value =
      this.#info.value.githubRepository !== '';
  }

  get githubInfo() {
    return this.#githubInfo;
  }

  get info() {
    return this.#info;
  }

  /// --- 검토 코드

  get isSuccessNotionDatabaseConnectionTest() {
    return this.#isSuccessNotionDatabaseConnectionTest;
  }

  get infoUpdateValidation() {
    return this.#infoUpdateValidation;
  }

  get isSuccessRepositoryNameCheck() {
    return this.#isSuccessRepositoryNameCheck;
  }

  async onManual() {
    if (this.#authStore.getAuth().value.isRequireInfo) {
      this.#apiResponseModalStore.onActive({
        isSuccess: false,
        message: '필수 정보를 입력하면 바로 서비스를 이용할 수 있어요! 🚀'
      });
      return;
    }
    this.#spinnerStore.on();
    const response = await onManualNogiApi();
    this.#spinnerStore.off();
    if (!response.isSuccess) {
      response.message = response.result;
    }
    this.#apiResponseModalStore.onActive(response);
  }

  async checkRepositoryName() {
    // todo: 이름 최소 길이, 최대 길이 체크
    if (this.#info.value.githubRepository.trim() === '') {
      this.#infoUpdateValidation.value.result.githubRepository =
        '이름을 입력해주세요.';
      return;
    }

    this.#spinnerStore.on();
    const params = { repositoryName: this.#info.value.githubRepository.trim() };
    const response = await checkValidationGithubRepositoryApi(params);
    this.#spinnerStore.off();

    if (response.isSuccess) {
      this.#isSuccessRepositoryNameCheck.value = true;
      this.initInfoUpdateValidation();
    }
    this.#apiResponseModalStore.onActive(response);
  }

  async updateNotificationAllow() {
    this.#info.value.isNotificationAllowed =
      !this.#info.value.isNotificationAllowed;
    await this.updateInfo();
  }

  async updateInfo() {
    this.#spinnerStore.on();
    const response = await updateUserInfoApi({
      notionBotToken: this.#info.value.notionBotToken,
      notionDatabaseId: this.#info.value.notionDatabaseId,
      githubRepository: this.#info.value.githubRepository,
      isNotificationAllowed: this.#info.value.isNotificationAllowed
    });
    this.#spinnerStore.off();
    this.#apiResponseModalStore.onActive(response);
    this.initInfoUpdateValidation();
    if (response.isSuccess) {
      this.#authStore.updateIsRequireInfo(false);
    }
  }

  checkUpdateInfoValidation() {
    const info = this.#info.value;

    if (!info.notionBotToken || !info.notionBotToken.trim()) {
      this.#infoUpdateValidation.value.result.notionBotToken =
        '꼭 필요한 정보에요!';
    }

    if (!info.notionDatabaseId || !info.notionDatabaseId.trim()) {
      this.#infoUpdateValidation.value.result.notionDatabaseId =
        '꼭 필요한 정보에요!';
    }

    if (!info.githubRepository || !info.githubRepository.trim()) {
      this.#infoUpdateValidation.value.result.githubRepository =
        '꼭 필요한 정보에요!';
    }

    if (!this.#isSuccessRepositoryNameCheck.value) {
      this.#infoUpdateValidation.value.result.githubRepository =
        'Repository 이름을 확인해주세요!';
    }

    if (!this.#isSuccessNotionDatabaseConnectionTest.value) {
      const message = 'Notion Database 연결 확인 해주세요!';
      this.#infoUpdateValidation.value.result.notionBotToken = message;
      this.#infoUpdateValidation.value.result.notionDatabaseId = message;
    }

    if (Object.keys(this.#infoUpdateValidation.value.result).length <= 0) {
      this.#infoUpdateValidation.value.isSuccess = true;
    }

    return this.#infoUpdateValidation;
  }

  initInfoUpdateValidation() {
    this.#infoUpdateValidation.value = {
      isSuccess: false,
      result: {}
    };
  }

  deleteUser() {
    this.#apiResponseModalStore.success({
      message: '회원 탈퇴를 원하시면 문의 사항에 남겨주시면 도와드리겠습니다'
    });
  }
}
