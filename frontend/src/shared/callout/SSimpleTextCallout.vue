<script setup>
defineProps({
  text: {
    type: String
  }
});
</script>

<template>
  <div class="text-xs sm:text-sm text-neutral">
    <font-awesome-icon class="mr-1" icon="fa-solid fa-circle-info" />
    <span>{{ text }}</span>
  </div>
</template>
