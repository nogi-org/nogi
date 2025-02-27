<script setup>
import Header from '@/views/layout/Header.vue';
import Footer from '@/views/layout/Footer.vue';
import { RouterView } from 'vue-router';
import ApiResponseAlerts from '@/components/modal/ApiResponseModal.vue';
import Spinner from '@/components/common/Spinner.vue';
import IssueButton from '@/components/buttons/IssueButton.vue';
import { useSpinnerStore } from '@/stores/spinnerStore.js';
const spinnerStore = useSpinnerStore();
</script>

<template>
  <div>
    <div class="border-b border-main">
      <Header class="layout" />
    </div>
    <!-- 현재 라우트가 Home일 경우 layout 및 py-12 스타일 제거 -->
    <div
      :class="{
        'layout': $route.name !== 'home',
        'py-12': $route.name !== 'home'
      }"
    >
      <RouterView />
    </div>
    <div>
      <Footer class="layout" />
    </div>
  </div>

  <Spinner v-if="spinnerStore.getStatus" />
  <ApiResponseAlerts />
  <IssueButton />
</template>

<style scoped>
.layout {
  @apply max-w-[1087px] m-auto px-5;
}
</style>
