<script setup>
import { RouterLink, useRoute, useRouter } from 'vue-router';
import { onMounted, ref, watch } from 'vue';
import SLoginButton from '@/shared/buttons/SLoginButton.vue';
import SLogoutButton from '@/shared/buttons/SLogoutButton.vue';
import { AuthManager } from '@/manager/auth/AuthManager.js';
import { useNavigationStore } from '@/stores/navigationStore.js';

const route = useRoute();
const router = useRouter();

const auth = new AuthManager();
const authInfo = auth.getAuthInfo();

const navigationStore = useNavigationStore();
const navigations = ref([]);

onMounted(() => {
  navigations.value = navigationStore.getHeaderNavigations(router);
  navigationStore.onActiveHeaderNavigation(navigations, route);
});

watch(route, () => {
  navigationStore.onActiveHeaderNavigation(navigations, route);
});
</script>

<template>
  <header>
    <!--첫줄-->
    <div class="flex justify-between items-center py-4">
      <h1 class="text-2xl tracking-widest sm:text-4xl">
        <RouterLink to="/">
          <img
            alt="로고 이미지"
            class="w-24 sm:w-28 md:w-32 lg:w-36"
            src="/assets/images/logo.png"
          />
        </RouterLink>
      </h1>
      <SLoginButton v-if="!authInfo" />
      <SLogoutButton v-if="authInfo" @onLogout="auth.onLogout()" />
    </div>

    <!--두번째줄-->
    <ul class="flex py-4 text-sm sm:text-base">
      <li v-for="item in navigations" class="mr-4 sm:mr-5 last:mr-0">
        <router-link :to="{ name: item.name }">
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
