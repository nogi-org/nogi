<script setup>
import { RouterView } from 'vue-router';
</script>

<template>
  <div class="flex gap-2">
    <ul class="w-[25%] border-r border-neutral">
      <li>설정 가이드</li>
      <li>이용 가이드</li>
    </ul>
    <div class="w-[75%]">
      <RouterView />
    </div>
  </div>
</template>
