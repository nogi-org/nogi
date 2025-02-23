import { useAuthStore } from '@/stores/authStore.js';
import {
  checkValidationGithubRepositoryApi,
  getUserInfoApi,
  onManualNogiApi,
  updateUserInfoApi
} from '@/api/user/user.js';
import { ref } from 'vue';
import { useSpinnerStore } from '@/stores/spinnerStore.js';
import { useApiResponseModalStore } from '@/stores/apiResponseModalStore.js';

export class UserManager {
  #authStore = useAuthStore();
  #apiResponseModalStore = useApiResponseModalStore();
  #spinnerStore = useSpinnerStore();
  #info = ref({});
  #checkRepositoryName = ref({
    isCheck: false,
    prevName: null
  });
  #infoUpdateValidation = ref({
    isResult: false,
    result: {}
  });

  async onManualNogi() {
    if (this.#authStore.getAuth().value.requireUserInfo) {
      this.#apiResponseModalStore.onActive({
        isSuccess: false,
        message: '필수 정보를 입력하면 바로 서비스를 이용할 수 있어요! 🚀'
      });
      return;
    }
    this.#spinnerStore.on();
    const response = await onManualNogiApi();
    this.#spinnerStore.off();
    this.#apiResponseModalStore.onActive(response);
  }

  async getInfo() {
    const response = await getUserInfoApi();
    this.#info.value = response.result;
    if (this.#info.value.githubRepository) {
      this.#checkRepositoryName.value.isCheck = true;
      this.#checkRepositoryName.value.prevName =
        this.#info.value.githubRepository;
    }
  }

  watchRepositoryName(value) {
    if (!value) return;
    this.#checkRepositoryName.value.isCheck =
      this.#checkRepositoryName.value.prevName === value;
  }

  async checkRepositoryName() {
    if (
      this.#checkRepositoryName.value.prevName ===
      this.#info.value.githubRepository
    ) {
      return;
    }
    // todo: 이름 최소 길이, 최대 길이 체크
    const params = { repositoryName: this.#info.value.githubRepository };
    const response = await checkValidationGithubRepositoryApi(params);
    this.#infoUpdateValidation.value.result.githubRepository = '';

    if (response.isSuccess) {
      this.#checkRepositoryName.value.isCheck = true;
      this.#checkRepositoryName.value.prevName =
        this.#info.value.githubRepository;
    }
    this.#apiResponseModalStore.onActive(response);
  }

  async updateInfo() {
    this.#spinnerStore.on();
    const response = await updateUserInfoApi({
      notionBotToken: this.#info.value.notionBotToken,
      notionDatabaseId: this.#info.value.notionDatabaseId,
      githubRepository: this.#info.value.githubRepository
    });
    this.#spinnerStore.off();
    this.#apiResponseModalStore.onActive(response);
    this.#initInfoUpdateValidation();
    if (response.isSuccess) {
      this.#authStore.updateRequireUserInfo(false);
    }
  }

  checkInfoValidation() {
    const info = this.#info.value;
    const validation = this.#infoUpdateValidation.value;
    validation.result = {};

    if (!info.notionBotToken || !info.notionBotToken.trim()) {
      validation.result.notionBotToken = '꼭 필요한 정보에요!';
    }

    if (!info.notionDatabaseId || !info.notionDatabaseId.trim()) {
      validation.result.notionDatabaseId = '꼭 필요한 정보에요!';
    }

    if (!info.githubRepository || !info.githubRepository.trim()) {
      validation.result.githubRepository = '꼭 필요한 정보에요!';
    }

    if (!this.#checkRepositoryName.value.isCheck) {
      validation.result.githubRepository = 'Repository 이름을 확인해주세요!';
    }

    if (Object.keys(validation.result).length <= 0) {
      validation.isResult = true;
    }
  }

  #initInfoUpdateValidation() {
    this.#infoUpdateValidation.value = {};
  }

  get info() {
    return this.#info;
  }

  get infoUpdateValidation() {
    return this.#infoUpdateValidation;
  }
}
