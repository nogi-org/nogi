import { defineStore } from 'pinia';
import { AuthManager } from '@/manager/auth/AuthManager.js';
import { useAuthStore } from '@/stores/authStore.js';

export const useNavigationStore = defineStore('useNavigationStore', () => {
  const authStore = useAuthStore();
  const LAYOUT_STYLES = {
    FULL: 'full'
  };

  const routes = [
    {
      path: '/authorize/redirect',
      name: 'authorizeRedirect',
      component: () => import('@/views/auth/VAuthorizeRedirect.vue')
    },
    {
      path: '/404',
      name: 'notFound',
      component: () => import('@/views/common/VNotFound.vue')
    },
    {
      path: '/:pathMatch(.*)*',
      redirect: '/404'
    },
    {
      path: '/use-policy',
      name: 'usePolicy',
      component: () => import('@/views/policy/VUsePolicy.vue')
    },
    {
      path: '/',
      name: 'frame',
      component: () => import('@/views/layout/LFrame.vue'),
      children: [
        {
          path: '/',
          name: 'home',
          meta: { layoutStyle: LAYOUT_STYLES.FULL },
          component: () => import('@/views/layout/LHome.vue')
        },
        {
          path: '/guide',
          name: 'guidePage',
          meta: {
            requiresLogin: false,
            headerNavigation: {
              title: '가이드',
              order: 1,
              isActive: false
            }
          },
          redirect: { name: 'setupGuidePage' },
          component: () => import('@/views/layout/LSubFrame.vue'),
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
              component: () => import('@/views/guide/VUsageGuide.vue')
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
              component: () => import('@/views/guide/VSetupGuide.vue')
            }
          ]
        },
        {
          path: '/notices',
          name: 'noticesPage',
          meta: {
            requiresLogin: false,
            headerNavigation: {
              title: '공지사항',
              order: 2,
              isActive: false
            }
          },
          component: () => import('@/views/notice/VNoticesPage.vue')
        },
        {
          path: '/notice/:noticeId',
          name: 'noticePage',
          meta: {
            requiresLogin: false
          },
          component: () => import('@/views/notice/VNoticePage.vue')
        },
        {
          path: '/notice/publish',
          name: 'noticePublishPage',
          meta: {
            requiresLogin: true,
            requiresRole: AuthManager.ROLE.ADMIN
          },
          component: () => import('@/views/notice/VNoticePublishPage.vue')
        },
        {
          path: '/my-page',
          name: 'myPage',
          meta: {
            requiresLogin: true,
            headerNavigation: {
              title: 'My Page',
              order: 3,
              isActive: false
            }
          },
          component: () => import('@/views/user/mypage/Demo1MyPage.vue')

          // todo: 아래 주석 풀어서 사용하시면 됩니다.
          // redirect: { name: 'demo1mypage' },
          // component: () => import('@/views/layout/LSubFrame.vue')
          // children: [
          //   {
          //     path: '/my-page/demo1',
          //     name: 'demo1mypage',
          //     meta: {
          //       subNavigation: {
          //         title: '다음버전',
          //         order: 2,
          //         isActive: false
          //       }
          //     },
          //     component: () => import('@/views/user/mypage/Demo1MyPage.vue')
          //   },
          //   {
          //     path: '/my-page/demo2',
          //     name: 'demo2mypage',
          //     meta: {
          //       subNavigation: {
          //         title: '기대해주세요 ✨ ',
          //         order: 2,
          //         isActive: false
          //       }
          //     },
          //     component: () => import('@/views/user/mypage/Demo2MyPage.vue')
          //   }
          // ]
        },

        // admin
        {
          path: '/admin',
          name: 'adminPage',
          meta: {
            requiresLogin: true,
            requiresRole: AuthManager.ROLE.ADMIN,
            headerNavigation: {
              title: '관리자',
              order: 4,
              isActive: false
            }
          },
          component: () => import('@/views/layout/LSubFrame.vue'),
          redirect: { name: 'newGuide' },
          children: [
            {
              path: 'guide/new',
              name: 'newGuide',
              meta: {
                subNavigation: {
                  title: '가이드 관리',
                  order: 2,
                  isActive: false
                }
              },
              component: () => import('@/views/guide/VNewGuide.vue')
            },
            {
              path: 'users',
              name: 'userInfo',
              meta: {
                subNavigation: {
                  title: '유저정보',
                  order: 1,
                  isActive: false
                }
              },
              redirect: { name: 'usersInfo' },
              component: () => import('@/views/user/info/LUserInfo.vue'),
              children: [
                {
                  path: '',
                  name: 'usersInfo',
                  component: () => import('@/views/user/info/VUsersInfo.vue')
                },
                {
                  path: ':userId/notion-pages',
                  name: 'userNotionPages',
                  component: () =>
                    import('@/views/user/info/VUserNotionPages.vue')
                }
              ]
            }
          ]
        },
        {
          path: '/setting',
          name: 'settingPage',
          meta: {
            requiresLogin: true,
            headerNavigation: {
              title: 'Setting',
              order: 5,
              isActive: false
            }
          },
          component: () => import('@/views/user/setting/VSetting.vue')
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
    const auth = authStore.getAuth().value;
    return router
      .getRoutes()
      .filter((route) => {
        const headerNavigation = route.meta?.headerNavigation;

        // 헤더 네비게이션만 가져오기
        if (!headerNavigation) return false;

        // 로그인해야 보이는 메뉴인데 로그인 안했으면 제외
        if (route.meta?.requiresLogin && !auth) {
          return false;
        }

        // 관리자 권한 확인
        if (route.meta?.requiresRole) {
          return (
            authStore.isAdmin() &&
            route.meta?.requiresRole === AuthManager.ROLE.ADMIN
          );
        }

        return true;
      })
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
    const matchedRoutes = route.matched;

    // 가장 가까운 subNavigation 그룹이 포함된 라우트를 찾음
    const subNavParent = matchedRoutes
      .slice()
      .reverse()
      .find((r) => r.children?.some((child) => child.meta?.subNavigation));

    if (!subNavParent) return [];

    return subNavParent.children
      .filter((r) => r.meta?.subNavigation)
      .map((r) => ({
        name: r.name,
        title: r.meta.subNavigation.title,
        isActive: r.name === route.name,
        order: r.meta.subNavigation.order ?? 0
      }))
      .sort((a, b) => a.order - b.order);
  }

  function onActiveSubNavigation(navigations, route) {
    const currentMatchedNames = route.matched.map((r) => r.name);

    navigations.value.forEach((nav) => {
      nav.isActive = currentMatchedNames.includes(nav.name);
    });
  }

  function onActiveHeaderNavigation(navigations, route) {
    const currentMatchedNames = route.matched.map((r) => r.name);

    navigations.value.forEach((navi) => {
      navi.isActive = currentMatchedNames.includes(navi.name);
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
