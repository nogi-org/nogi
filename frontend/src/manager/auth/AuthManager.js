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
  #apiResponseModalStore = useNotifyStore();
  #authStore = useAuthStore();
  #spinnerStore = useSpinnerStore();
  #LOGIN_TYPE = {
    GITHUB: 'GITHUB',
    NOTION: 'NOTION'
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
    this.#spinnerStore.on();
    const response = await getNotionLoginURL();
    window.location.href = response.result;
    this.#spinnerStore.off();
  }

  // 로그인 실패 시 페이지 이동
  // todo: 문구 바꾸기
  async processLoginFail(type) {
    const routerName = type === this.#LOGIN_TYPE.GITHUB ? 'home' : 'myPage';
    await this.#router.push({ name: routerName });

    const message =
      type === this.#LOGIN_TYPE.GITHUB
        ? '앗, GitHub 로그인이 실패했어요. 다시 한번 시도해 주세요!'
        : 'Notion 로그인에 실패했습니다. 다시 시도해 주세요.';
    this.#apiResponseModalStore.fail({
      message: message
    });
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
    const routeName = auth.isRequireInfo ? 'myPage' : 'home';
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
