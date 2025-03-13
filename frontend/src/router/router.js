import { createRouter, createWebHistory } from 'vue-router';
import { useAuthStore } from '@/stores/authStore.js';
import { useRoutesStore } from '@/stores/navigationStore.js';

const router = createRouter({
  history: createWebHistory(),
  scrollBehavior() {
    return { top: 0 };
  },
  routes: []
});

// store에 정의한 routes를 가져와서 등록
export function setupRouter() {
  const routesStore = useRoutesStore();
  routesStore.getRoutes().forEach((route) => {
    router.addRoute(route);
  });
}

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();
  const auth = authStore.getAuth().value;
  if (to.meta?.requiresAuth && !auth) {
    // 로그인 필요한 페이지인데 로그인하지 않은 경우
    next('/');
  } else if (to.meta?.requiresRole && to.meta?.requiresRole !== auth.role) {
    // 권한이 부족 경우
    next('/');
  } else {
    // 정상적으로 라우팅 진행
    next();
  }
});

export default router;
