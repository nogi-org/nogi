import {
  getMeApi,
  getUsersAdminApi,
  onManualSyncApi,
  updateMeApi
} from '@/api/user/user.js';
import { ref } from 'vue';
import { useSpinnerStore } from '@/stores/spinnerStore.js';
import { useNotifyStore } from '@/stores/notifyStore.js';

export class UserManager {
  // 스토어
  #notifyModal = useNotifyStore();
  #spinner = useSpinnerStore();

  // 변수
  #me = ref({});
  #users = ref({});

  get users() {
    return this.#users;
  }

  get me() {
    return this.#me;
  }

  async getMe() {
    this.#spinner.on();
    const response = await getMeApi();
    this.#spinner.off();
    this.#me.value = response.result;
  }

  async getUsers() {
    this.#users.value = await getUsersAdminApi();
  }

  async updateMe(param) {
    this.#spinner.on();
    const response = await updateMeApi(param);
    this.#spinner.off();
    this.#notifyModal.onActive(response);
  }

  async updateNotificationAllow() {
    this.#me.value.isNotificationAllowed =
      !this.#me.value.isNotificationAllowed;
    await this.updateMe({
      isNotificationAllowed: this.#me.value.isNotificationAllowed
    });
  }

  deleteMe() {
    this.#notifyModal.success({
      message: '회원 탈퇴를 원하시면 문의 사항에 남겨주시면 도와드리겠습니다'
    });
  }

  async onManualAsync(isConnectedNotion, isConnectedGithub) {
    if (!isConnectedNotion || !isConnectedGithub) {
      this.#notifyModal.fail({
        message: 'Notion과 GitHub을 모두 연결상태면 이용할 수 있어요.'
      });
      return;
    }
    this.#spinner.on();
    const response = await onManualSyncApi();
    this.#spinner.off();
    this.#notifyModal.onActive(response);
  }
}
