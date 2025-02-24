<script setup>
import { RouterLink, useRoute } from 'vue-router';
import { onMounted, watch, watchEffect } from 'vue';
import { useNavigationStore } from '@/stores/navigationStore.js';
import LoginButton from '@/components/buttons/LoginButton.vue';
import LogoutButton from '@/components/buttons/LogoutButton.vue';
import { AuthManager } from '@/manager/auth/AuthManager.js';

const route = useRoute();

const auth = new AuthManager();
const authInfo = auth.getAuthInfo();

const navigationStore = useNavigationStore();
const navigations = navigationStore.getNavigations();

onMounted(() => {
  navigationStore.setIsVisibleByAuth();
});

watch(
  () => authInfo.value,
  newValue => {
    navigationStore.setIsVisibleByAuth();
  },
  { deep: true }
);

watchEffect(() => {
  navigationStore.setIsActiveByRoute(route);
});
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
      <LoginButton v-if="!authInfo" @onLogin="auth.toGithubLoginPage()" />
      <LogoutButton v-if="authInfo" @onLogout="auth.onLogout()" />
    </div>

    <!--두번째줄-->
    <ul class="flex py-4 text-sm sm:text-base">
      <li class="mr-4 sm:mr-5 last:mr-0" v-for="item in navigations">
        <router-link :to="{ name: item.routeName }">
          <span
            :class="{
              'font-noto_sans_m relative after:block after:w-full after:h-[0.3px] after:bg-white after:mt-1 after:content-[\'\']':
                item.isActive
            }"
          >
            {{ item.title }}
          </span>
        </router-link>
      </li>
    </ul>
  </header>
</template>
