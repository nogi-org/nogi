<script setup>
import Header from '@/views/layout/Header.vue';
import Footer from '@/views/layout/Footer.vue';
import { RouterView, useRoute } from 'vue-router';
import SApiResponseModal from '@/shared/modal/SApiResponseModal.vue';
import SSpinner from '@/shared/spinner/SSpinner.vue';
import SIssueButton from '@/shared/buttons/SIssueButton.vue';
import { useSpinnerStore } from '@/stores/spinnerStore.js';
import { onMounted, ref, watch } from 'vue';
import { useRoutesStore } from '@/stores/navigationStore.js';

const route = useRoute();
const spinnerStore = useSpinnerStore();
const routesStore = useRoutesStore();
const layoutStyle = ref('');

onMounted(() => {
  layoutStyle.value = routesStore.createLayoutStyle(route);
});

watch(route, (value) => {
  layoutStyle.value = routesStore.createLayoutStyle(value);
});
</script>

<template>
  <div>
    <div class="border-b border-main">
      <Header class="layout" />
    </div>
    <div :class="layoutStyle">
      <RouterView />
    </div>
    <div>
      <Footer class="layout" />
    </div>
  </div>

  <SSpinner v-if="spinnerStore.getStatus" />
  <SApiResponseModal />
  <SIssueButton />
</template>

<style scoped>
.layout {
  @apply max-w-[1087px] m-auto px-5;
}
</style>
