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
      class="bg-action text-xs px-2.5 py-1 rounded-md sm:text-sm"
      @click="action"
    >
      {{ name }}
    </button>
  </div>
</template>
