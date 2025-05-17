import { useSpinnerStore } from '@/stores/spinnerStore.js';
import { useNotifyStore } from '@/stores/notifyStore.js';
import {
  createDatabaseProperty,
  getConnectedNotionApi,
  getNotionDatabaseAdminApi,
  getNotionDetailPageAdminApi,
  getNotionPagesAdminApi,
  updateNotionPageIdAdminApi
} from '@/api/notion/notion.js';
import { ref } from 'vue';

export class NotionManager {
  // 스토어
  #notifyModal = useNotifyStore();
  #spinner = useSpinnerStore();

  // 변수
  #connection = ref();
  #pages = ref();
  #page = ref();
  #database = ref();
  #prevCursors = ref([]);

  get database() {
    return this.#database;
  }

  get page() {
    return this.#page;
  }

  get connection() {
    return this.#connection;
  }

  get pages() {
    return this.#pages;
  }

  get prevCursors() {
    return this.#prevCursors;
  }

  setRichTextProperty(value) {
    const key = value ? value : '값을 입력해주세요.';
    const property = `{"properties":{"${key}":{"rich_text":{}}}}`;
    return JSON.parse(property);
  }

  async createRichTextProperty(value) {
    const payload = JSON.stringify(value);
    this.#spinner.on();
    const response = await createDatabaseProperty(payload);
    this.#spinner.off();
    this.#notifyModal.onActive({ message: response });
  }

  async getConnectedNotion() {
    const response = await getConnectedNotionApi();
    this.#connection.value = response.result.isConnection;
  }

  async updateNotionPageId() {
    this.#spinner.on();
    const response = await updateNotionPageIdAdminApi();
    this.#spinner.off();
    const success = response.success?.join();
    const fail = response.fail?.join();
    this.#notifyModal.onActive({ message: `성공: ${success}\n실패: ${fail}` });
  }

  async getPagesOfUser(params) {
    // 다음 버튼
    if (params.isNext) {
      this.#prevCursors.value.push(this.#pages.value.pages[0].id);
    }

    // 이전버튼
    if (!params.isNext && this.#prevCursors.value.length > 0) {
      params.nextCursor = this.#prevCursors.value.pop();
    }

    this.#spinner.on();
    this.#pages.value = await getNotionPagesAdminApi({
      userId: params.userId,
      nextCursor: params.nextCursor
    });
    this.#spinner.off();
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  async getDetailPage(params) {
    this.#spinner.on();
    const response = await getNotionDetailPageAdminApi(params);
    this.#spinner.off();
    this.#page.value = JSON.parse(response);
  }

  async getNotionDatabase(userId) {
    this.#spinner.on();
    const response = await getNotionDatabaseAdminApi(userId);
    this.#spinner.off();
    this.#database.value = JSON.parse(response);
  }
}
