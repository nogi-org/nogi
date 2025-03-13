<script setup>
defineProps({
  notice: {
    type: String,
    default: '알수없음'
  }
});
</script>

<template>
  <div
    class="text-danger font-noto_sans_r text-xs neon animate-blink flex items-center"
  >
    <font-awesome-icon icon="fa-regular fa-bell" class="mr-1" />
    <span class="">{{ notice }}</span>
  </div>
</template>
