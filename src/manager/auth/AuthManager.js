import { getGithubLoginURL } from '@/api/auth/auth.js';
import { useAuthStore } from '@/stores/authStore.js';
import { apiResponseModalStore } from '@/stores/modalStore.js';
import { useRouter } from 'vue-router';
import { useSpinnerStore } from '@/stores/spinnerStore.js';

export class AuthManager {
  #router = useRouter();
  #apiResponseModal = apiResponseModalStore();
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

  // todo: 로컬 스토리지에 유저 정보 삭제(401 에러가 발생한경우 또는 로그아웃 한 경우)
  // todo: 서버에서는 로그아웃하면 쿠키 삭제 필요
  // 접속 정보 삭제
  deleteAuthInfo() {
    this.#authStore.deleteAuth();
  }

  // 로그인 성공 후 페이지 이동
  async goPageAfterSuccessLogin(requireUserInfo) {
    this.#spinnerStore.on();
    const routeName = requireUserInfo ? 'myPage' : 'home';
    await this.#router.push({ name: routeName });
    if (requireUserInfo) {
      this.#apiResponseModal.onActive({
        isStatus: true,
        code: 'SUCCESS',
        message:
          '환영합니다!\n원활한 서비스 이용을 위해 필요한 정보를 입력해 주세요.'
      });
    }
    this.#spinnerStore.off();
  }

  onLogout() {
    console.log('로그아웃 : ');
  }
}
