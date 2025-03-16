<script setup>
const props = defineProps({
  isOn: {
    type: Boolean,
    default: false
  }
});
const emit = defineEmits(['action']);

const action = () => {
  emit('action', !props.isOn);
};
</script>

<template>
  <div>
    <button
      :class="{ 'bg-green-500': isOn, 'bg-neutral': !isOn }"
      class="relative w-10 h-6 flex items-center border border-main rounded-full transition-colors ms-auto"
      @click="action"
    >
      <span
        :class="{ 'translate-x-4': isOn }"
        class="absolute left-0.5 w-4 h-4 bg-white rounded-full shadow-md transition-transform"
      />
    </button>
  </div>
</template>
