import { defineStore } from 'pinia';
import { AuthManager } from '@/manager/auth/AuthManager.js';

export const useNavigationStore = defineStore('useNavigationStore', () => {
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
          path: '/usage-guide',
          name: 'GuidePage',
          meta: {
            headerNavigation: {
              title: '가이드',
              order: 1,
              isActive: false
            }
          },
          component: () => import('@/views/guide/GuidePage.vue'),
          children: [
            {
              path: '/usage-guide',
              name: 'usageGuidePage',
              meta: {},
              component: () => import('@/views/guide/UsageGuidePage.vue')
            },
            {
              path: '/setup-guide',
              name: 'setupGuidePage',
              meta: {},
              component: () => import('@/views/guide/SetupGuidePage.vue')
            }
          ]
        },
        {
          path: '/my-page',
          name: 'myPage',
          meta: {
            requiresAuth: true,
            headerNavigation: {
              title: 'My Page',
              order: 2,
              isActive: false
            }
          },
          component: () => import('@/views/user/mypage/MyPage.vue')
        },
        {
          path: '/admin',
          name: 'adminPage',
          meta: {
            requiresRole: AuthManager.ROLE.ADMIN,
            headerNavigation: {
              title: '관리자',
              order: 4,
              isActive: false
            }
          },
          component: () => import('@/views/admin/AdminPage.vue')
        },
        {
          path: '/setting',
          name: 'settingPage',
          meta: {
            requiresAuth: true,
            headerNavigation: {
              title: 'Setting',
              order: 3,
              isActive: false
            }
          },
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

  function getHeaderNavigations(router) {
    return router
      .getRoutes()
      .filter((route) => route?.meta?.headerNavigation)
      .map((route) => {
        return {
          name: route.name,
          title: route.meta.headerNavigation.title,
          isActive: route.meta.headerNavigation.isActive,
          order: route.meta.headerNavigation.order
        };
      })
      .sort((a, b) => a.order - b.order);
  }

  function onActiveHeaderNavigation(navigations, name) {
    navigations.value.forEach((navi) => (navi.isActive = navi.name === name));
  }

  return {
    getRoutes,
    createLayoutStyle,
    getHeaderNavigations,
    onActiveHeaderNavigation
  };
});
