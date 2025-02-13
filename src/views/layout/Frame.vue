<script setup>
import Header from '@/views/layout/Header.vue';
import Footer from '@/views/layout/Footer.vue';
import { RouterView } from 'vue-router';
import ApiResponseAlerts from '@/components/modal/ApiResponseModal.vue';
import Spinner from '@/components/common/Spinner.vue';
import { useSpinnerStore } from '@/stores/spinnerStore.js';
const spinnerStore = useSpinnerStore();
</script>

<template>
  <div>
    <div class="border-b border-main">
      <Header class="layout" />
    </div>
    <div class="layout py-12">
      <RouterView />
    </div>
    <div>
      <Footer class="layout" />
    </div>
  </div>

  <Spinner v-if="spinnerStore.getStatus" />
  <ApiResponseAlerts />
</template>

<style scoped>
.layout {
  @apply max-w-[1087px] m-auto px-5;
}
</style>
