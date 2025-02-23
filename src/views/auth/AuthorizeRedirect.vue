<script setup>
import { onMounted } from 'vue';
import { AuthManager } from '@/manager/auth/AuthManager.js';

const auth = new AuthManager();

onMounted(async () => {
  const { requireUserInfo, userId, role } = auth.getLoginInfoFromRedirectURL();
  auth.setAuthInfo(requireUserInfo, userId, role);
  await auth.goPageAfterSuccessLogin();
});
</script>

<template>
  <div class="relative min-h-screen">
    <h1
      class="text-xl absolute left-1/2 top-1/2 transform -translate-x-1/2 -translate-y-1/2"
    >
      로그인 중...
    </h1>
  </div>
</template>
