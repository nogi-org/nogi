import { defineStore } from 'pinia';
import { reactive } from 'vue';

export const useNavigationStore = defineStore('useNavigationStore', () => {
  const navigations = reactive([
    {
      title: '사용 가이드',
      routeName: 'userGuidePage',
      category: 'userGuide',
      hasActive: false
    },
    {
      title: '내정보',
      routeName: 'myPage',
      category: 'myPage',
      hasActive: false
    },
    {
      title: '관리자',
      routeName: 'adminPage',
      category: 'admin',
      hasActive: false
    }
  ]);

  function getNavigation() {
    return navigations;
  }

  function setNavigationActiveByRoute(route) {
    const category = route.meta.category;
    navigations.find((navigation) => {
      navigation.hasActive = navigation.category === category;
    });
  }

  return {
    getNavigation,
    setNavigationActiveByRoute
  };
});
