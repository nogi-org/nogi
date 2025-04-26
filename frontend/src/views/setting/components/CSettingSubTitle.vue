<script setup>
defineProps({
  title: {
    type: String,
    default: 'Setting Title'
  }
});
</script>

<template>
  <div>
    <h3 class="font-noto_sans_m mb-2">{{ title }}</h3>
  </div>
</template>
