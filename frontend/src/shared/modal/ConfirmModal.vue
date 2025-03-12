<script setup>
import { onMounted, ref, watch } from 'vue';
import { initFlowbite, Modal } from 'flowbite';

const props = defineProps({
  action: {
    type: Object
  }
});
let modal;
const modalEl = ref(null);
const emit = defineEmits(['onAction']);

onMounted(() => {
  initFlowbite();
  modal = createModal(modalEl.value);
});

watch(
  () => props.action.hasActive,
  hasActive => {
    hasActive ? modal.show() : modal.hide();
  }
);

const createModal = modalEl => {
  const options = {
    onHide: () => onAction(false)
  };
  return new Modal(modalEl, options);
};

const onAction = hasAction => {
  emit('onAction', hasAction);
};
</script>

<template>
  <div
    ref="modalEl"
    tabindex="-1"
    class="hidden overflow-y-auto overflow-x-hidden fixed top-0 right-0 left-0 z-50 justify-center items-center w-full md:inset-0 h-[calc(100%-1rem)] max-h-full"
  >
    <div class="relative p-4 w-full max-w-md max-h-full">
      <div class="relative bg-white rounded-lg shadow">
        <button
          type="button"
          @click="modal.hide()"
          class="absolute top-3 end-2.5 text-gray-400 bg-transparent hover:bg-gray-200 hover:text-gray-900 rounded-lg text-sm w-8 h-8 ms-auto inline-flex justify-center items-center"
        >
          <svg
            class="w-3 h-3"
            aria-hidden="true"
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 14 14"
          >
            <path
              stroke="currentColor"
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="m1 1 6 6m0 0 6 6M7 7l6-6M7 7l-6 6"
            />
          </svg>
          <span class="sr-only">Close modal</span>
        </button>
        <div class="p-4 md:p-5 text-center">
          <h3 class="mb-5 text-sm text-gray-500 py-2 sm:text-base">
            {{ action.message }}
          </h3>
          <div class="flex gap-3 justify-center">
            <button
              type="button"
              @click="onAction(false)"
              class="text-gray-500 border border-gray-500 rounded-md text-xs inline-flex items-center px-4 py-1.5 text-center sm:px-5 sm:py-2.5"
            >
              취소
            </button>
            <button
              type="button"
              @click="onAction(true)"
              class="text-white bg-rose-500 rounded-md text-xs inline-flex items-center px-4 py-1.5 text-center sm:px-5 sm:py-2.5"
            >
              확인
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
