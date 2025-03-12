<script setup>
defineProps({
  text: {
    type: String
  }
});
</script>

<template>
  <div class="text-xs sm:text-sm text-danger font-noto_sans_m">
    <font-awesome-icon class="mr-1" icon="fa-solid fa-triangle-exclamation" />
    <span>{{ text }}</span>
  </div>
</template>
