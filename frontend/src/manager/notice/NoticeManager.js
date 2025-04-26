import { ref } from 'vue';
import { useSpinnerStore } from '@/stores/spinner.js';
import {
  getNoticeApi,
  getNoticeRecipientsAdminApi,
  getNoticesApi,
  noticePublishAdminApi
} from '@/api/notice/notice.js';
import { useRouter } from 'vue-router';
import { useNotifyStore } from '@/stores/notifyModal.js';

export class NoticeManager {
  // 스토어
  #spinner = useSpinnerStore();
  #notifyModal = useNotifyStore();

  // 변수
  #newNotice = ref({
    title: '',
    content: '',
    url: import.meta.env.VITE_WEB_BASE_URL + '/notice'
  });
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

  async loadNotices(page) {
    this.#spinner.on();

    this.#pagination.page = page;
    const response = await getNoticesApi({
      size: this.#pagination.size,
      page: this.#pagination.page
    });
    this.#notices.value = response.content;
    this.#pagination.total = response.totalElements;

    this.#spinner.off();
  }

  async loadNotice(noticeId) {
    this.#spinner.on();
    this.#notice.value = await getNoticeApi(noticeId);
    this.#spinner.off();
  }

  async loadNoticeRecipients(noticeId, page) {
    // todo: 권한체크 후 어드민 경우만 조회
    this.#recipientsPagination.page = page;
    const response = await getNoticeRecipientsAdminApi(noticeId, {
      size: this.#recipientsPagination.size,
      page: this.#recipientsPagination.page
    });
    console.log('response -> ', response);
    this.#recipients.value = response.content;
    this.#recipientsPagination.total = response.totalElements;
  }

  setNewNoticeContent(content) {
    this.#newNotice.value.content = content;
  }

  isValidationPublish() {
    if (this.#newNotice.value.content.text === '') {
      alert('내용을 입력해주세요.');
      return false;
    }
    if (this.#newNotice.value.title === '') {
      alert('제목을 입력해주세요.');
      return false;
    }
    return true;
  }

  async publish() {
    this.#spinner.on();
    const response = await noticePublishAdminApi({
      ...this.#newNotice.value,
      content: this.#newNotice.value.content.html
    });
    this.#notifyModal.onActive({ message: response.message });
    this.#spinner.off();
    await this.#router.push({ name: 'noticesPage' });
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

  getNewNotice() {
    return this.#newNotice;
  }

  getRecipientsPagination() {
    return this.#recipientsPagination;
  }

  async goToNotice(id) {
    await this.#router.push({ name: 'noticePage', params: { noticeId: id } });
  }
}
