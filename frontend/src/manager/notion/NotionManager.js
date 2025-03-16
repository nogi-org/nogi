import { useSpinnerStore } from '@/stores/spinnerStore.js';
import { useNotifyStore } from '@/stores/notifyStore.js';
import { onDatabaseConnectTest } from '@/api/notion/notion.js';

export class NotionManager {
  #notifyStore = useNotifyStore();
  #spinnerStore = useSpinnerStore();

  async onDatabaseConnectionTest() {
    this.#spinnerStore.on();
    const response = await onDatabaseConnectTest();
    this.#spinnerStore.off();
    this.#notifyStore.onActive(response);
  }
}
