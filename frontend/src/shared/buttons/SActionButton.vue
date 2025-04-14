<script setup>
defineProps({
  name: {
    type: String,
    default: '버튼'
  }
});
const emit = defineEmits(['action']);
const action = () => {
  emit('action');
};
</script>

<template>
  <div>
    <button
      class="bg-action px-3 py-2 rounded-md text-white text-xs"
      @click="action"
    >
      {{ name }}
    </button>
  </div>
</template>
