import { ref } from 'vue';
import { useSpinnerStore } from '@/stores/spinnerStore.js';
import {
  getNoticeApi,
  getNoticeRecipientsApi,
  getNoticesApi
} from '@/api/notice/notice.js';
import { useRouter } from 'vue-router';

export class NoticeManager {
  #notices = ref();
  #notice = ref();
  #pagination = {
    page: 0,
    size: 10,
    total: 0
  };
  #recipients = ref();
  #recipientsPagination = {
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

  async loadNoticeRecipients(noticeId, page) {
    // todo: 권한체크 후 어드민 경우만 조회
    this.#recipientsPagination.page = page;
    const response = await getNoticeRecipientsApi(noticeId, {
      size: this.#recipientsPagination.size,
      page: this.#recipientsPagination.page
    });
    console.log('response -- > ', response);
    this.#recipients.value = response.content;
    this.#recipientsPagination.total = response.totalElements;
  }

  goToPublishPage() {
    this.#router.push({ name: 'noticePublishPage' });
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

  getRecipients() {
    return this.#recipients;
  }

  getRecipientsPagination() {
    return this.#recipientsPagination;
  }

  async goToNotice(id) {
    await this.#router.push({ name: 'noticePage', params: { noticeId: id } });
  }
}
