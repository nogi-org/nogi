import { useAuthStore } from '@/stores/authStore.js';
import { apiGetUserInfo } from '@/api/user/user.js';
import { reactive } from 'vue';
import defaultProfile from '/assets/images/default_profile.png';
import { useSpinnerStore } from '@/stores/spinnerStore.js';
import { useApiResponseModalStore } from '@/stores/apiResponseModalStore.js';

export class UserManager {
  #authStore = useAuthStore();
  #apiResponseModal = useApiResponseModalStore();
  #spinnerStore = useSpinnerStore();
  #profile = reactive({
    externalId: '',
    profileImage: '',
    nickname: '',
    role: '',
    hasOwner: false
  });

  #info = reactive({
    notionBotToken: '',
    databaseKey: '',
    role: ''
  });

  setNotionBotToken(value) {
    this.#info.notionBotToken = value;
  }

  setDatabaseKey(value) {
    this.#info.databaseKey = value;
  }

  async loadMyInfo(externalId) {
    let info;
    if (this.#hasOwner(externalId)) {
      // 내정보
      info = this.#authStore.getAuth;
      this.#profile.hasOwner = true;
    } else {
      // TODO: 서버에서 유저정보가져오기
      const response = await this.getUserProfile(externalId);
      info = response.data;
      this.#profile.hasOwner = false;
    }
    this.#profile.nickname = info.nickname;
    this.#profile.profileImage = info.profileImage
      ? info.profileImage
      : defaultProfile;
    this.#profile.role = info.role;
    this.#profile.externalId = info.externalId;
  }

  async getUserProfile(externalId) {
    return await apiGetUserInfo(externalId);
  }

  #hasOwner(externalId) {
    return this.#authStore.hasOwner(externalId);
  }

  checkInfoValidation() {
    const validation = {
      isResult: true,
      result: {}
    };

    if (!this.#info.notionBotToken.trim()) {
      validation.isResult = false;
      validation.result['notionBotToken'] = '꼭 필요한 정보에요!';
    }

    if (!this.#info.databaseKey.trim()) {
      validation.isResult = false;
      validation.result['databaseKey'] = '꼭 필요한 정보에요!';
    }

    return validation;
  }
}
