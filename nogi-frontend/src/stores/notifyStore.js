import { defineStore } from 'pinia';
import { computed, ref } from 'vue';

export const useNotifyStore = defineStore('useNotifyStore', () => {
  // 모달 활성화 여부
  const hasActive = ref(false);

  // 모달 내용(에러, 성공 모달 구분)
  const contents = ref({
    isSuccess: true,
    code: '',
    message: ''
  });

  const getActive = computed(() => {
    return { hasActive: hasActive.value, contents: contents.value };
  });

  function onActive(payload) {
    contents.value.isSuccess = payload.isSuccess;
    setCommonContents(payload);
  }

  function success(payload) {
    contents.value.isSuccess = true;
    setCommonContents(payload);
  }

  function fail(payload) {
    contents.value.isSuccess = false;
    setCommonContents(payload);
  }

  function offActive() {
    hasActive.value = false;
    contents.value = {};
  }

  function setCommonContents(payload) {
    contents.value.code = payload.code;
    contents.value.message = payload.message;
    hasActive.value = true;
  }

  return { getActive, onActive, offActive, success, fail };
});
