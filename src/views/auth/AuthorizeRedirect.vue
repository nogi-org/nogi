<script setup>
import { onMounted } from 'vue';
import { AuthManager } from '@/manager/auth/AuthManager.js';

onMounted(async () => {
  const authManager = new AuthManager();
  const code = new URL(window.location.href).searchParams.get('code');
  await authManager.login(code);
});
</script>

<template>
  <div class="relative min-h-screen">
    <h1 class="text-xl absolute left-1/2 top-1/2 transform -translate-x-1/2 -translate-y-1/2">
      로그인 중...
    </h1>
  </div>
</template>
