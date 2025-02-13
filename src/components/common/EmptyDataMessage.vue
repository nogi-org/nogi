<script setup>
defineProps({
  message: {
    type: String,
    default: '데이터가 없습니다.'
  }
});
</script>

<template>
  <p class="text-xs text-gray-500 text-center sm:text-sm">
    {{ message }}
  </p>
</template>
