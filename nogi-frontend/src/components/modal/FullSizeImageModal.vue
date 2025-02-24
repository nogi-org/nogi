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
  (hasActive) => {
    hasActive ? modal.show() : modal.hide();
  }
);

const createModal = (modalEl) => {
  const options = {
    onHide: () => onAction(false)
  };
  return new Modal(modalEl, options);
};

const onAction = (hasAction) => {
  emit('onAction', hasAction);
};
</script>

<template>
  <div
    ref="modalEl"
    tabindex="-1"
    class="hidden overflow-y-auto overflow-x-hidden fixed top-0 right-0 left-0 z-50 justify-center items-center w-full md:inset-0 h-[calc(100%-1rem)] max-h-full"
  >
    <div class="relative w-full max-w-md max-h-full text-right sm:max-w-2xl">
      <button
        type="button"
        @click="modal.hide()"
        class="mb-3 bg-transparent text-white rounded-lg text-sm w-8 h-8 ms-auto inline-flex justify-center items-center"
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
      </button>
      <img
        :src="action.message"
        alt="유저 이미지 전체화면"
        class="w-full h-full"
      />
    </div>
  </div>
</template>
