import { useSpinnerStore } from '@/stores/spinnerStore.js';
import { useNotifyStore } from '@/stores/notifyStore.js';
import {
  getConnectedNotionApi,
  updateNotionPageIdAdminApi
} from '@/api/notion/notion.js';
import { ref } from 'vue';

export class NotionManager {
  // 스토어
  #notifyModal = useNotifyStore();
  #spinner = useSpinnerStore();

  // 변수
  #connection = ref({});

  get connection() {
    return this.#connection;
  }

  async getConnectedNotion() {
    this.#spinner.on();
    const response = await getConnectedNotionApi();
    this.#spinner.off();
    this.#notifyModal.onActive(response);
  }

  async updateNotionPageId() {
    this.#spinner.on();
    const response = await updateNotionPageIdAdminApi();
    this.#spinner.off();
    const success = response.success?.join();
    const fail = response.fail?.join();
    this.#notifyModal.onActive({ message: `성공: ${success}\n실패: ${fail}` });
  }
}
