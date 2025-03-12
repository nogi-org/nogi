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
    <h2 class="font-noto_sans_b text-lg">{{ title }}</h2>
    <hr class="border-main my-2" />
  </div>
</template>
