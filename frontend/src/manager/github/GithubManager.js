import { getConnectedGithubApi } from '@/api/github/github.js';
import { useSpinnerStore } from '@/stores/spinnerStore.js';
import { ref } from 'vue';
import { useNotifyStore } from '@/stores/notifyStore.js';
import { isEmptyText } from '@/utils/stringValidation.js';

export class GithubManager {
  // 스토어
  #spinner = useSpinnerStore();
  #notifyModal = useNotifyStore();

  // 변수
  #connection = ref({});

  get connection() {
    return this.#connection;
  }

  async getConnectedGithub() {
    this.#spinner.on();
    this.#connection.value = await getConnectedGithubApi();
    this.#spinner.off();
  }

  isSaveValidationRepositoryName(name) {
    if (isEmptyText(name)) {
      this.#notifyModal.fail({
        message: 'Github Repository 이름을 입력해주세요.'
      });
      return false;
    }
    return true;
  }

  isSaveValidationOwner(owner) {
    if (isEmptyText(owner)) {
      this.#notifyModal.fail({
        message: 'Github Owner를 입력해주세요.'
      });
      return false;
    }
    return true;
  }

  isSaveValidationEmail(email) {
    if (isEmptyText(email)) {
      this.#notifyModal.fail({
        message: 'Github 이메일을 입력해주세요.'
      });
      return false;
    }
    return true;
  }
}
