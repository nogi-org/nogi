<script setup>
import LFooter from '@/views/layout/LFooter.vue';
import { RouterView, useRoute } from 'vue-router';
import SApiResponseModal from '@/shared/modal/SApiResponseModal.vue';
import SSpinner from '@/shared/spinner/SSpinner.vue';
import SIssueButton from '@/shared/buttons/SIssueButton.vue';
import { useSpinnerStore } from '@/stores/spinnerStore.js';
import { onMounted, ref, watch } from 'vue';
import { useNavigationStore } from '@/stores/navigationStore.js';
import LHeader from '@/views/layout/LHeader.vue';

const route = useRoute();
const spinnerStore = useSpinnerStore();
const navigationStore = useNavigationStore();
const layoutStyle = ref('');

onMounted(() => {
  layoutStyle.value = navigationStore.createLayoutStyle(route);
});

watch(route, (value) => {
  layoutStyle.value = navigationStore.createLayoutStyle(value);
});
</script>

<template>
  <div>
    <div class="border-b border-main">
      <LHeader class="layout" />
    </div>
    <div :class="layoutStyle">
      <RouterView />
    </div>
    <div>
      <LFooter class="layout" />
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
