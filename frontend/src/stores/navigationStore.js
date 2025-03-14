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
          path: '/guide',
          name: 'guidePage',
          meta: {
            headerNavigation: {
              title: '가이드',
              order: 1,
              isActive: false
            }
          },
          redirect: { name: 'setupGuidePage' },
          component: () => import('@/views/layout/SubFrame.vue'),
          children: [
            {
              path: '/guide/usage',
              name: 'usageGuidePage',
              meta: {
                subNavigation: {
                  title: '사용 가이드',
                  order: 2,
                  isActive: false
                }
              },
              component: () => import('@/views/guide/UsageGuidePage.vue')
            },
            {
              path: '/guide/setup',
              name: 'setupGuidePage',
              meta: {
                subNavigation: {
                  title: '설정 가이드',
                  order: 1,
                  isActive: false
                }
              },
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
          redirect: { name: 'demo1mypage' },
          component: () => import('@/views/layout/SubFrame.vue'),
          children: [
            {
              path: '/my-page/demo1',
              name: 'demo1mypage',
              meta: {
                subNavigation: {
                  title: '업데이트중',
                  order: 2,
                  isActive: false
                }
              },
              component: () => import('@/views/user/mypage/Demo1MyPage.vue')
            },
            {
              path: '/my-page/demo2',
              name: 'demo2mypage',
              meta: {
                subNavigation: {
                  title: '업데이트중',
                  order: 2,
                  isActive: false
                }
              },
              component: () => import('@/views/user/mypage/Demo2MyPage.vue')
            }
          ]
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
          component: () => import('@/views/layout/SubFrame.vue'),
          redirect: { name: 'adminGuidePage' },
          children: [
            {
              path: '/admin/guide',
              name: 'adminGuidePage',
              meta: {
                subNavigation: {
                  title: '가이드 관리',
                  order: 1,
                  isActive: false
                }
              },
              component: () => import('@/views/admin/AdminGuidePage.vue')
            },
            {
              path: '/admin/user',
              name: 'adminUserPage',
              meta: {
                subNavigation: {
                  title: '유저 관리',
                  order: 2,
                  isActive: false
                }
              },
              component: () => import('@/views/admin/AdminUserPage.vue')
            }
          ]
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
    const defaultStyle = 'max-w-[1200px] m-auto px-5 py-12';
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

  function getSubNavigations(route) {
    const parentRoute =
      route.matched.find(
        (r, index, arr) => arr[index + 1]?.name === route.name
      ) || [];

    return parentRoute.children
      .map((route) => ({
        name: route.name,
        title: route.meta.subNavigation.title,
        isActive: route.meta.subNavigation.isActive,
        order: route.meta.subNavigation.order
      }))
      .sort((a, b) => a.order - b.order);
  }

  function onActiveSubNavigation(navigations, name) {
    navigations.value.forEach((navi) => (navi.isActive = navi.name === name));
  }

  function onActiveHeaderNavigation(navigations, route) {
    const parentRoute =
      route.matched.find(
        (r, index, arr) => arr[index + 1]?.name === route.name
      ) || [];

    // route.matched 가 2개 아래면 단일 페이지, 2개 이상이면 서브 네비게이션이 있는 페이지
    const isParentActive = route.matched.length > 2;
    navigations.value.forEach((navi) => {
      navi.isActive = isParentActive
        ? navi.name === parentRoute.name
        : navi.name === route.name;
    });
  }

  return {
    getRoutes,
    createLayoutStyle,
    getHeaderNavigations,
    onActiveHeaderNavigation,
    getSubNavigations,
    onActiveSubNavigation
  };
});
