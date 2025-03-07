<script setup>
import { onMounted } from 'vue';
import { AuthManager } from '@/manager/auth/AuthManager.js';
import { useRoute, useRouter } from 'vue-router';

const auth = new AuthManager();
const router = useRouter();
const route = useRoute();

onMounted(async () => {
  route.query.startLogin
    ? await auth.toGithubLoginPage()
    : await processRedirect();
});

const processRedirect = async () => {
  const {
    isSuccess,
    isRequireNotionInfo,
    isRequireGithubInfo,
    userId,
    role,
    type
  } = auth.getLoginInfoFromRedirectURL();

  if (!isSuccess) {
    await auth.processLoginFail(type);
    return;
  }

  auth.setAuthInfo(isRequireGithubInfo, userId, role);
  await auth.toNotionLoginPage(isRequireNotionInfo);
  await auth.goPageAfterSuccessLogin(isRequireNotionInfo);
};
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
