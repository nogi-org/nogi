import { defineStore } from 'pinia';
import { computed, ref } from 'vue';

export const useAuthStatusStore = defineStore('useAuthStatusStore', () => {
  const AUTH_KEY = 'auth';
  const auth = ref(
    localStorage.getItem(AUTH_KEY) ? JSON.parse(localStorage.getItem(AUTH_KEY)) : null
  );

  const getAuth = computed(() => auth.value);

  function setAuth(payload) {
    localStorage.setItem(AUTH_KEY, JSON.stringify(payload));
    auth.value = payload;
  }

  function deleteAuth() {
    localStorage.removeItem('auth');
    auth.value = null;
  }

  function hasOwner(externalId) {
    return auth.value?.externalId === externalId;
  }

  function hasAdmin() {
    return auth.value?.role === 'MASTER' || auth.value?.role === 'MANAGER';
  }

  function updateAuth(payload) {
    if (payload.profilePath != null) {
      auth.value.profileImage = payload.profilePath;
    } else if (payload.nickname != null) {
      auth.value.nickname = payload.nickname;
    }
    setAuth(auth.value);
  }

  function hasOnLogin() {
    return auth.value != null;
  }

  return { getAuth, setAuth, deleteAuth, hasOwner, hasAdmin, updateAuth, hasOnLogin };
});
