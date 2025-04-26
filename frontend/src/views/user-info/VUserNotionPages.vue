<script setup>
import { onMounted, ref } from 'vue';
import { getUserNotionPages } from '@/api/notion/notion.js';

const demo = ref('');

onMounted(async () => {
  const response = await getUserNotionPages({
    userId: 29,
    pageId: '1d8a9cc8-1056-811f-b46c-e16cc083e7eb'
  });
  demo.value = JSON.parse(response);
});
</script>

<template>
  <div>노션 유저 페이지!!!</div>
  <div>{{ demo.object }}</div>
  <hr />
  <div>{{ demo }}</div>
</template>

<style scoped></style>
