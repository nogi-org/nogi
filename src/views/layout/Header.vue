<script setup>
import { RouterLink, useRoute } from 'vue-router';
import { ref, watch, watchEffect } from 'vue';
import { useAuthStatusStore } from '@/stores/authStore.js';
import { useNavigationStore } from '@/stores/navigationStore.js';
import LoginButton from '@/components/buttons/LoginButton.vue';
import LogoutButton from '@/components/buttons/LogoutButton.vue';

const route = useRoute();

const authStatusStore = useAuthStatusStore();
const hasJoin = ref(authStatusStore.getAuth);

const navigationStore = useNavigationStore();
const navigations = navigationStore.getNavigation();

watch(
  () => authStatusStore.getAuth,
  () => {
    hasJoin.value = authStatusStore.getAuth;
  }
);

watchEffect(() => {
  navigationStore.setNavigationActiveByRoute(route);
});

const onLogin = () => {
  console.log('로그인!!!!');
};
</script>

<template>
  <header>
    <!--첫줄-->
    <div class="flex justify-between items-center py-4">
      <h1 class="text-2xl tracking-widest sm:text-4xl">
        <RouterLink to="/">
          <img
            src="/assets/images/logo.png"
            class="w-24 sm:w-28 md:w-32 lg:w-36"
            alt="로고 이미지"
          />
        </RouterLink>
      </h1>
      <LoginButton v-if="!hasJoin" @onLogin="onLogin" />
      <LogoutButton v-if="hasJoin" @onLogout="onLogout" />
    </div>

    <!--두번째줄-->
    <ul class="flex py-4 text-sm sm:text-base">
      <li class="mr-4 sm:mr-5 last:mr-0" v-for="item in navigations">
        <router-link :to="{ name: item.routeName }">
          <span
            :class="{
              'font-noto_sans_m relative after:block after:w-full after:h-[0.3px] after:bg-white after:mt-1 after:content-[\'\']':
                item.hasActive
            }"
          >
            {{ item.title }}
          </span>
        </router-link>
      </li>
    </ul>
  </header>
</template>
