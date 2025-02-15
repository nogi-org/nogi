import { defineStore } from 'pinia';
import { computed, reactive } from 'vue';
import { AuthManager } from '@/manager/auth/AuthManager.js';
import { useAuthStore } from '@/stores/authStore.js';

export const useNavigationStore = defineStore('useNavigationStore', () => {
  const authStore = useAuthStore();
  const navigations = reactive([
    {
      title: '사용 가이드',
      routeName: 'userGuidePage',
      category: 'userGuide',
      isActive: false,
      isVisible: true
    },
    {
      title: '내정보',
      routeName: 'myPage',
      category: 'myPage',
      isActive: false,
      isVisible: false
    },
    {
      title: '관리자',
      routeName: 'adminPage',
      category: 'admin',
      isActive: false,
      isVisible: false
    }
  ]);

  function getNavigations() {
    return computed(() => navigations.filter(item => item.isVisible));
  }

  function setIsActiveByRoute(route) {
    const category = route.meta.category;
    navigations.find(navigation => {
      navigation.isActive = navigation.category === category;
    });
  }

  function setIsVisibleByAuth() {
    const myPage = navigations.find(nav => nav.category === 'myPage');
    const adminPage = navigations.find(nav => nav.category === 'admin');

    if (!authStore.getAuth().value) {
      if (myPage) myPage.isVisible = false;
      if (adminPage) adminPage.isVisible = false;
      return;
    }

    const { userId = null, role = '' } = authStore.getAuth().value;

    if (myPage) myPage.isVisible = !!userId;
    if (adminPage) adminPage.isVisible = role === AuthManager.ROLE.ADMIN;
  }

  return {
    getNavigations,
    setIsActiveByRoute,
    setIsVisibleByAuth
  };
});
