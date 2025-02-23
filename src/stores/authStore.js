import { defineStore } from 'pinia';
import { ref } from 'vue';
import { AuthManager } from '@/manager/auth/AuthManager.js';

export const useAuthStore = defineStore('useAuthStore', () => {
  const auth = ref(
    localStorage.getItem(AuthManager.AUTH_KEY)
      ? JSON.parse(localStorage.getItem(AuthManager.AUTH_KEY))
      : null
  );

  function getAuth() {
    if (auth.value !== null) {
      auth.value.requireUserInfo = auth.value.requireUserInfo === 'true';
    }
    return auth;
  }

  function deleteAuth() {
    auth.value = null;
    localStorage.removeItem(AuthManager.AUTH_KEY);
  }

  function setAuth(payload) {
    auth.value = payload;
    localStorage.setItem(AuthManager.AUTH_KEY, JSON.stringify(payload));
  }

  function updateRequireUserInfo(isRequire) {
    auth.value.requireUserInfo = isRequire;
    localStorage.setItem(AuthManager.AUTH_KEY, JSON.stringify(auth.value));
  }

  return {
    getAuth,
    deleteAuth,
    setAuth,
    updateRequireUserInfo
  };
});
