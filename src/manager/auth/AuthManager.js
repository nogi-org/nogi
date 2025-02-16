import { apiLogout, getGithubLoginURL } from '@/api/auth/auth.js';
import { useAuthStore } from '@/stores/authStore.js';
import { useRouter } from 'vue-router';
import { useSpinnerStore } from '@/stores/spinnerStore.js';
import { useApiResponseModalStore } from '@/stores/apiResponseModalStore.js';
import { ApiResponse } from '@/api/apiResponse.js';

export class AuthManager {
  #router = useRouter();
  #apiResponseModalStore = useApiResponseModalStore();
  #authStore = useAuthStore();
  #spinnerStore = useSpinnerStore();

  static get AUTH_KEY() {
    return 'auth';
  }

  static get ROLE() {
    return {
      ADMIN: 'ADMIN',
      USER: 'USER'
    };
  }

  // github 로그인 URL 요청
  async toGithubLoginPage() {
    this.#spinnerStore.on();
    window.location.href = await getGithubLoginURL();
    this.#spinnerStore.off();
  }

  // 서버의 리다이렉트로 받은 URL 정보로 셋팅
  getLoginInfoFromRedirectURL() {
    const searchParams = new URLSearchParams(window.location.search);
    return Object.fromEntries(searchParams.entries());
  }

  // 접속 정보 저장
  setAuthInfo(requireUserInfo, userId, role) {
    if (requireUserInfo == null || !userId || !role) {
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

  // 로그인 성공 후 페이지 이동
  async goPageAfterSuccessLogin(requireUserInfo) {
    this.#spinnerStore.on();
    const routeName = requireUserInfo ? 'myPage' : 'home';
    await this.#router.push({ name: routeName });
    this.#noticeLogin(requireUserInfo);
    this.#spinnerStore.off();
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

  #noticeLogin(requireUserInfo) {
    const message = requireUserInfo
      ? '환영합니다!👏\n원활한 서비스 이용을 위해 필요한 정보를 입력해 주세요.'
      : '환영합니다!💫\nNOGI의 멋진 기능들을 마음껏 즐겨보세요!';

    this.#apiResponseModalStore.onActive({
      isSuccess: true,
      code: ApiResponse.S_0,
      message: message
    });
  }
}
