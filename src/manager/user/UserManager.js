import { useAuthStore } from '@/stores/authStore.js';
import {
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
        message: '아래 필수정보를 등록해주세요!'
      });
    }

    // todo: 결과 알림
    const response = await onManualNogiApi();
    console.log('수동실행 결과: ', response);
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

    // const response = await checkUserGithubRepositoryApi();
    const response = { result: true };
    this.#infoUpdateValidation.value.result.githubRepository = '';

    if (response.result) {
      this.#checkRepositoryName.value.isCheck = true;
      this.#checkRepositoryName.value.prevName =
        this.#info.value.githubRepository;
    }
    // TODO: 알림(사용가능한 이름입니다. or 이미 사용중인 이름입니다.)
  }

  async updateInfo() {
    this.#spinnerStore.on();
    const response = await updateUserInfoApi({
      notionAuthToken: this.#info.value.notionAuthToken,
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

    if (!info.notionAuthToken || !info.notionAuthToken.trim()) {
      validation.result.notionAuthToken = '꼭 필요한 정보에요!';
    }

    if (!info.notionDatabaseId || !info.notionDatabaseId.trim()) {
      validation.result.notionDatabaseId = '꼭 필요한 정보에요!';
    }

    if (!info.githubRepository || !info.githubRepository.trim()) {
      validation.result.githubRepository = '꼭 필요한 정보에요!';
    }

    if (!this.#checkRepositoryName.value.isCheck) {
      validation.result.githubRepository = '이름을 확인해주세요!';
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
