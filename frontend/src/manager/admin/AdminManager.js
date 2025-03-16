import { updateNotionPageIdOfAllUser } from '@/api/admin/admin.js';
import { useNotifyStore } from '@/stores/notifyStore.js';
import { useSpinnerStore } from '@/stores/spinnerStore.js';

export class AdminManager {
  #notifyStore = useNotifyStore();
  #spinnerStore = useSpinnerStore();

  // todo: 리팩토링 필요
  async updateNotionPageIdOfAllUser() {
    this.#spinnerStore.on();
    const response = await updateNotionPageIdOfAllUser();
    this.#spinnerStore.off();
    const success = response.success?.join();
    const fail = response.fail?.join();
    this.#notifyStore.onActive({ message: `성공: ${success}\n실패: ${fail}` });
  }
}
