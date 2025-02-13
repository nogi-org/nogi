<script setup>
defineProps({
  title: {
    type: String,
    default: '버튼이름 필요!'
  },
  bgColor: {
    type: String,
    default: 'bg-main'
  }
});
</script>

<template>
  <button type="button" class="text-xs px-3 py-2 text-white tracking-widest font-medium rounded-lg sm:px-2.5 sm:text-sm" :class="bgColor">
    {{ title }}
  </button>
</template>

<style scoped></style>
