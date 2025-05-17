<script setup>
import { onMounted, ref } from 'vue';
import { AuthManager } from '@/manager/auth/AuthManager.js';
import { useRoute } from 'vue-router';

const auth = new AuthManager();
const route = useRoute();
const displayText = ref(
  '로그인을 준비하고 있어요...\n잠시만 기다려 주세요! ⏳'
);

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
    type,
    message,
    event // [노션 로그인] 또는 [노션 새로연결] case 를 판단하기 위한 플래그값
  } = auth.getLoginInfoFromRedirectURL();

  displayText.value = auth.createDisplayTextOfAuthPage();

  // 노션 새로운 데이터베이스 생성 프로세스
  if (auth.isNewNotionDatabaseEvent(event)) {
    await auth.processNewNotionDatabaseEvent(isSuccess, message);
    return;
  }

  // 깃허브 로그인 및 노션 로그인 프로세스 실패 경우
  if (!isSuccess) {
    await auth.processLoginFail(type, message);
    return;
  }

  auth.setAuthInfo(isRequireGithubInfo, userId, role);
  await auth.toNotionLoginPage(isRequireNotionInfo);
  await auth.goPageAfterSuccessLogin(isRequireNotionInfo);
};
</script>

<template>
  <div class="flex items-center justify-center min-h-screen">
    <div class="text-center">
      <div class="flex items-center justify-center space-x-3">
        <font-awesome-icon
          icon="spinner"
          spin
          size="2x"
          class="text-white opacity-80"
        />
        <h1
          class="text-2xl font-semibold text-white drop-shadow-md whitespace-pre-line"
        >
          {{ displayText }}
        </h1>
      </div>
    </div>
  </div>
</template>
