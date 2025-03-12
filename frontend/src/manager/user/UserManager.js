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

  async getInfo() {
    this.#spinnerStore.on();
    const response = await getUserInfoApi();
    this.#spinnerStore.off();
    this.#info.value = response.result;
  }

  async saveRepositoryName(name) {
    // todo: 레파지토리 이름에 맞게 벨리데이션 추가 필요, 글자 수
    if (this.#isEmptyText(name)) {
      this.#apiResponseModalStore.fail({
        message: 'Github Repository 이름을 입력해주세요.'
      });
      return;
    }
    await this.#updateGithubInfo({ githubRepository: name });
  }

  async saveGithubOwner(owner) {
    // todo: owner 글자 수 체크
    if (this.#isEmptyText(owner)) {
      this.#apiResponseModalStore.fail({
        message: 'Github Owner를 입력해주세요.'
      });
      return;
    }
    await this.#updateGithubInfo({ githubOwner: owner });
  }

  async saveGithubEmail(email) {
    // todo: 이메일 양식 체크
    if (this.#isEmptyText(email)) {
      this.#apiResponseModalStore.fail({
        message: 'Github 이메일을 입력해주세요.'
      });
      return;
    }
    await this.#updateGithubInfo({ githubEmail: email });
  }

  async updateInfo(param) {
    this.#spinnerStore.on();
    const response = await updateUserInfoApi(param);
    this.#spinnerStore.off();
    this.#apiResponseModalStore.onActive(response);
  }

  async getConnectedGithubInfo() {
    this.#spinnerStore.on();
    this.#githubInfo.value = await getConnectedGithubInfoApi();
    this.#spinnerStore.off();
  }

  async updateNotificationAllow() {
    this.#info.value.isNotificationAllowed =
      !this.#info.value.isNotificationAllowed;
    await this.updateInfo({
      isNotificationAllowed: this.#info.value.isNotificationAllowed
    });
  }

  deleteUser() {
    this.#apiResponseModalStore.success({
      message: '회원 탈퇴를 원하시면 문의 사항에 남겨주시면 도와드리겠습니다'
    });
  }

  get githubInfo() {
    return this.#githubInfo;
  }

  get info() {
    return this.#info;
  }

  #isEmptyText(text) {
    return !text || !text.trim();
  }

  async #updateGithubInfo(param) {
    await this.updateInfo(param);
    await this.getConnectedGithubInfo();
  }

  /// --- 검토 코드

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
      // this.#infoUpdateValidation.value.result.githubRepository =
      //   '이름을 입력해주세요.';
      return;
    }

    this.#spinnerStore.on();
    const params = { repositoryName: this.#info.value.githubRepository.trim() };
    const response = await checkValidationGithubRepositoryApi(params);
    this.#spinnerStore.off();

    if (response.isSuccess) {
      // this.#isSuccessRepositoryNameCheck.value = true;
      // this.initInfoUpdateValidation();
    }
    this.#apiResponseModalStore.onActive(response);
  }
}
