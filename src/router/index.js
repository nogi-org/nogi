import { createRouter, createWebHistory } from 'vue-router';

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
          meta: { category: 'myPage' },
          component: () => import('@/views/user/MyPage.vue')
        },
        {
          path: '/admin',
          name: 'adminPage',
          meta: { category: 'admin' },
          component: () => import('@/views/admin/AdminPage.vue')
        }
      ]
    }
  ]
});

router.beforeEach((to, from, next) => {
  // console.log(`to: ${to}, from: ${from}, next: ${next}`);
  next();
});

export default router;
