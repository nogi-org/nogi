import { defineStore } from 'pinia';
import { computed, ref } from 'vue';

export const useSpinnerStore = defineStore('useSpinnerStore', () => {
  const requestCount = ref(0);

  const getStatus = computed(() => requestCount.value > 0);

  function on() {
    requestCount.value += 1;
  }

  function off() {
    if (requestCount.value > 0) {
      requestCount.value -= 1;
    }
  }

  return { getStatus, on, off };
});
