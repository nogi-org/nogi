<script setup>
import { onMounted } from 'vue';
import { AuthManager } from '@/manager/auth/AuthManager.js';
import { useRouter } from 'vue-router';

const auth = new AuthManager();
const router = useRouter();

onMounted(async () => {
  const { isSuccess, isRequireNotionInfo, requireUserInfo, userId, role } =
    auth.getLoginInfoFromRedirectURL();
  if (!isSuccess) {
    router.push({ name: 'home' });
    return;
  }
  auth.setAuthInfo(requireUserInfo, userId, role);
  await auth.toNotionLoginPage(isRequireNotionInfo);
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
