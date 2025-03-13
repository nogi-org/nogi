import { defineStore } from 'pinia';
import { AuthManager } from '@/manager/auth/AuthManager.js';

export const useRoutesStore = defineStore('routesStore', () => {
  const LAYOUT_STYLES = {
    FULL: 'full'
  };

  const routes = [
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
          meta: { layoutStyle: LAYOUT_STYLES.FULL },
          component: () => import('@/views/layout/Home.vue')
        },
        {
          path: '/',
          name: 'GuidePage',
          meta: { category: 'userGuide' },
          component: () => import('@/views/guide/GuidePage.vue'),
          children: [
            {
              path: '/user-guide',
              name: 'userGuidePage',
              meta: { category: 'userGuide' },
              component: () => import('@/views/guide/UsageGuidePage.vue')
            }
          ]
        },
        {
          path: '/my-page',
          name: 'myPage',
          meta: { category: 'myPage', requiresAuth: true },
          component: () => import('@/views/user/mypage/MyPage.vue')
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
          component: () => import('@/views/user/setting/SettingPage.vue')
        }
      ]
    }
  ];

  function getRoutes() {
    return routes;
  }

  // frame에 routerView 넓이 처리
  function createLayoutStyle(route) {
    const defaultStyle = 'max-w-[1280px] m-auto px-5 py-12';
    const fullStyle = 'w-full';

    return route.meta.layoutStyle === LAYOUT_STYLES.FULL
      ? fullStyle
      : defaultStyle;
  }

  return {
    getRoutes,
    createLayoutStyle
  };
});
