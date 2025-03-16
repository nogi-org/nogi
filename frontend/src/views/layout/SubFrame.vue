<script setup>
import { RouterLink, RouterView, useRoute } from 'vue-router';
import { useNavigationStore } from '@/stores/navigationStore.js';
import { onMounted, ref, watch } from 'vue';

const route = useRoute();

const navigationStore = useNavigationStore();
const navigations = ref([]);

onMounted(() => {
  initNavigation();
});

watch(route, () => {
  initNavigation();
});

const initNavigation = () => {
  navigations.value = navigationStore.getSubNavigations(route);
  navigationStore.onActiveSubNavigation(navigations, route.name);
};
</script>

<template>
  <div class="sm:flex">
    <ul
      class="mb-10 flex flex-col gap-2 border-b border-main sm:border-b-0 sm:w-[20%]"
    >
      <li v-for="item in navigations" class="w-full last:mb-0">
        <router-link :to="{ name: item.name }">
          <p
            class="px-2 py-1.5 text-sm"
            :class="{
              'font-noto_sans_m bg-gray-900 rounded-md': item.isActive
            }"
          >
            {{ item.title }}
          </p>
        </router-link>
      </li>
    </ul>

    <div class="sm:w-[80%] sm:pl-8">
      <RouterView />
    </div>
  </div>
</template>
