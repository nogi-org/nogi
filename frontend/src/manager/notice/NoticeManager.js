import { ref } from 'vue';
import { useSpinnerStore } from '@/stores/spinnerStore.js';
import { getNoticeApi, getNoticesApi } from '@/api/notice/notice.js';
import { useRouter } from 'vue-router';

export class NoticeManager {
  #notices = ref();
  #notice = ref();
  #pagination = {
    page: 0,
    size: 10,
    total: 0
  };
  #router = useRouter();
  #spinnerStore = useSpinnerStore();

  async loadNotices(page) {
    this.#spinnerStore.on();

    this.#pagination.page = page;
    const response = await getNoticesApi({
      size: this.#pagination.size,
      page: this.#pagination.page
    });
    this.#notices.value = response.content;
    this.#pagination.total = response.totalElements;

    this.#spinnerStore.off();
  }

  async loadNotice(noticeId) {
    this.#spinnerStore.on();
    this.#notice.value = await getNoticeApi(noticeId);
    this.#spinnerStore.off();
  }

  getNotices() {
    return this.#notices;
  }

  getNotice() {
    return this.#notice;
  }

  getPagination() {
    return this.#pagination;
  }

  async goToNotice(id) {
    await this.#router.push({ name: 'noticePage', params: { noticeId: id } });
  }
}
