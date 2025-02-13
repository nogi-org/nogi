import router from '@/router/index.js';
import { apiLoginKakao, apiLogout } from '@/api/auth/auth.js';
import { useAuthStatusStore } from '@/stores/authStore.js';
import { apiResponseModalStore } from '@/stores/modalStore.js';
import { useSpinnerStore } from '@/stores/spinnerStore.js';

export class AuthManager {
  #redirectUri = import.meta.env.VITE_CLIENT_BASE_URL + import.meta.env.VITE_KAKAO_JOIN_REDIRECT_PATH;
  #authStatusStore = useAuthStatusStore();
  #apiResponseModal = apiResponseModalStore();
  #spinnerStore = useSpinnerStore();

  static getToken() {
    const authStatusStore = useAuthStatusStore();
    return 'Bearer ' + authStatusStore.getAuth?.token;
  }

  async login(code) {
    const payload = { code: code, redirectUri: this.#redirectUri };

    this.#spinnerStore.on();
    const response = await apiLoginKakao(payload);
    this.#spinnerStore.off();

    if (response.success) {
      router.push({ name: 'home' }).then(() => this.#authStatusStore.setAuth(response.data));
    } else {
      router.push({ name: 'home' }).then(() => this.#apiResponseModal.onActive(response));
    }
  }

  async logout() {
    this.#spinnerStore.on();
    const response = await apiLogout();
    this.#spinnerStore.off();

    if (response.success) {
      router.push({ name: 'home' }).then(() => this.#authStatusStore.deleteAuth());
    } else {
      router.push({ name: 'home' }).then(() => this.#apiResponseModal.onActive(response));
    }
  }

  initKakao() {
    window.Kakao.Auth.authorize({ redirectUri: this.#redirectUri });
  }
}
