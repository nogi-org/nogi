import { defineStore } from 'pinia';
import { computed, ref } from 'vue';

export const apiResponseModalStore = defineStore(
  'apiResponseModalStore',
  () => {
    const hasActive = ref(false); // 모달 활성화 여부
    const contents = ref({}); // 모달 내용(에러, 성공 모달 구분)

    const getActive = computed(() => {
      return { hasActive: hasActive.value, contents: contents.value };
    });

    function onActive(payload) {
      contents.value = {
        hasStatus: payload.success,
        code: payload.code,
        message: payload.message
      };
      hasActive.value = true;
    }

    function offActive() {
      hasActive.value = false;
      contents.value = {};
    }

    return { getActive, onActive, offActive };
  }
);
