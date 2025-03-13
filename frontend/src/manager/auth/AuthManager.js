import {
  apiLogout,
  getGithubLoginURL,
  getNotionLoginURL
} from '@/api/auth/auth.js';
import { useAuthStore } from '@/stores/authStore.js';
import { useRouter } from 'vue-router';
import { useSpinnerStore } from '@/stores/spinnerStore.js';
import { useNotifyStore } from '@/stores/notifyStore.js';
import { ApiResponse } from '@/api/apiResponse.js';

export class AuthManager {
  #router = useRouter();
  #routerName = {
    setting: 'settingPage',
    home: 'home'
  };
  #apiResponseModalStore = useNotifyStore();
  #authStore = useAuthStore();
  #spinnerStore = useSpinnerStore();
  #LOGIN_TYPE = {
    GITHUB: 'GITHUB',
    NOTION: 'NOTION'
  };

  // [노션 로그인] 또는 [노션 새로연결] case 를 판단하기 위한 플래그값
  #NOTION_AUTH_EVENT = {
    LOGIN: 'login',
    NEW: 'new'
  };

  static get AUTH_KEY() {
    return 'nogi_auth';
  }

  static get ROLE() {
    return {
      ADMIN: 'ADMIN',
      USER: 'USER'
    };
  }

  /*
  github 로그인 URL 요청
  로그인 요청하면 깃허브 로그인 URL을 서버에서 받음 -> 깃허브 로그인 페이지로 이동
  -> 로그인 -> 서버에서 AuthorizeRedirect 페이지로 리다이렉트 -> 로그인 후 작업처리(프론트)
   */
  async toGithubLoginPage() {
    const response = await getGithubLoginURL();
    window.location.href = response.result;
  }

  // 노션 로그인 페이지로 이동
  async toNotionLoginPage(isRequireNotionInfo) {
    if (!isRequireNotionInfo) {
      return;
    }
    const response = await getNotionLoginURL({
      event: this.#NOTION_AUTH_EVENT.LOGIN
    });
    window.location.href = response.result;
  }

  // 새로운 노션 데이터베이스 생성 페이지로 이동
  async toNotionPageForCreateNewDatabase() {
    const response = await getNotionLoginURL({
      event: this.#NOTION_AUTH_EVENT.NEW
    });
    window.location.href = response.result;
  }

  isNewNotionDatabaseEvent(event) {
    return this.#NOTION_AUTH_EVENT.NEW === event;
  }

  createDisplayTextOfAuthPage(event) {
    return this.#NOTION_AUTH_EVENT.NEW === event
      ? '새로운 Database 생성중....'
      : '로그인중....';
  }

  async processNewNotionDatabaseEvent(isSuccess, message) {
    // 성공, 실패 상관없이 setting 페이지로
    await this.#router.push({ name: 'settingPage' });
    this.#apiResponseModalStore.onActive({
      isSuccess: isSuccess,
      message: isSuccess ? '새로운 Notion Database를 연결 완료했어요.' : message
    });
  }

  // 로그인 실패 시 페이지 이동
  async processLoginFail(type, message) {
    const routerName =
      type === this.#LOGIN_TYPE.GITHUB
        ? this.#routerName.home
        : this.#routerName.setting;
    await this.#router.push({ name: routerName });
    this.#apiResponseModalStore.fail({ message: message });
  }

  // 깃헙, 노션 리다이렉트 경우 url 파라미터로 정보 추출
  getLoginInfoFromRedirectURL() {
    const searchParams = new URLSearchParams(window.location.search);
    const params = Object.fromEntries(searchParams.entries());
    params.isSuccess = params.isSuccess === 'true';
    params.isRequireNotionInfo = params.isRequireNotionInfo === 'true';
    params.isRequireGithubInfo = params.isRequireGithubInfo === 'true';
    return params;
  }

  // 깃헙 로그인 시 정보 저장
  setAuthInfo(requireUserInfo, userId, role) {
    if (!userId || !role) {
      return;
    }
    this.#authStore.setAuth({ requireUserInfo, userId, role });
  }

  // 접속 정보 조회
  getAuthInfo() {
    return this.#authStore.getAuth();
  }

  // 접속 정보 삭제
  deleteAuthInfo() {
    this.#authStore.deleteAuth();
  }

  // 로그인 완료 후 페이지로 이동(노션 로그인 완료 후 최종 페이지로 이동)
  async goPageAfterSuccessLogin(isRequireNotionInfo) {
    if (isRequireNotionInfo) {
      return;
    }

    const auth = this.#authStore.getAuth().value;
    const routeName = auth.isRequireInfo
      ? this.#routerName.setting
      : this.#routerName.home;
    await this.#router.push({ name: routeName });
    this.#noticeLogin(auth.isRequireInfo);
  }

  // 로그아웃
  async onLogout() {
    this.#spinnerStore.on();
    const response = await apiLogout();
    this.deleteAuthInfo();
    this.#spinnerStore.off();
    await this.#router.push({ name: 'home' });
    this.#apiResponseModalStore.onActive(response);
  }

  #noticeLogin(isRequireInfo) {
    const message = isRequireInfo
      ? '환영합니다!👏\n원활한 서비스 이용을 위해 GitHub Repository 정보를 입력해주세요.'
      : '환영합니다!💫\nNOGI의 멋진 기능들을 마음껏 즐겨보세요!';

    this.#apiResponseModalStore.success({
      code: ApiResponse.S_0,
      message: message
    });
  }
}
