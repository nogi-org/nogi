import { createRouter, createWebHistory } from 'vue-router';
import { AuthManager } from '@/manager/auth/AuthManager.js';
import { useAuthStore } from '@/stores/authStore.js';

const router = createRouter({
  history: createWebHistory(),
  scrollBehavior() {
    return { top: 0 };
  },
  routes: [
    {
      path: '/authorize/redirect',
      name: 'authorizeRedirect',
      component: () => import('@/views/auth/AuthorizeRedirect.vue')
    },
    {
      path: '/404',
      name: 'notFound',
      component: () => import('@/views/common/NotFound.vue')
    },
    {
      path: '/:pathMatch(.*)*',
      redirect: '/404'
    },
    {
      path: '/use-policy',
      name: 'usePolicy',
      component: () => import('@/views/policy/UsePolicy.vue')
    },
    {
      path: '/',
      name: 'frame',
      component: () => import('@/views/layout/Frame.vue'),
      children: [
        {
          path: '/',
          name: 'home',
          component: () => import('@/views/layout/Home.vue')
        },
        {
          path: '/user-guide',
          name: 'userGuidePage',
          meta: { category: 'userGuide' },
          component: () => import('@/views/user/UserGuidePage.vue')
        },
        {
          path: '/my-page',
          name: 'myPage',
          meta: { category: 'myPage', requiresAuth: true },
          component: () => import('@/views/user/MyPage.vue')
        },
        {
          path: '/admin',
          name: 'adminPage',
          meta: {
            category: 'admin',
            requiresRole: AuthManager.ROLE.ADMIN
          },
          component: () => import('@/views/admin/AdminPage.vue')
        },
        {
          path: '/setting',
          name: 'settingPage',
          meta: { category: 'setting', requiresAuth: true },
          component: () => import('@/views/user/SettingPage.vue')
        }
      ]
    }
  ]
});

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
