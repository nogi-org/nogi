import { defineStore } from 'pinia';
import { computed, ref } from 'vue';

export const useSpinnerStore = defineStore('useSpinnerStore', () => {
  const hasOn = ref(false);

  const getStatus = computed(() => hasOn.value);

  function on() {
    hasOn.value = true;
  }

  function off() {
    hasOn.value = false;
  }

  return { getStatus, on, off };
});
