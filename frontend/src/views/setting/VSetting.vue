<script setup>
import { onBeforeMount, provide } from 'vue';
import { UserManager } from '@/manager/user/UserManager.js';
import CNotionSetting from '@/views/setting/components/CNotionSetting.vue';
import CGithubSetting from '@/views/setting/components/CGithubSetting.vue';
import CActionSetting from '@/views/setting/components/CActionSetting.vue';
import CNoticeSetting from '@/views/setting/components/CNoticeSetting.vue';
import CDangerZoneSetting from '@/views/setting/components/CDangerSetting.vue';
import { AuthManager } from '@/manager/auth/AuthManager.js';
import { NotionManager } from '@/manager/notion/NotionManager.js';
import { GithubManager } from '@/manager/github/GithubManager.js';

const auth = new AuthManager();
const user = new UserManager();
const notion = new NotionManager();
const github = new GithubManager();

provide('authManager', auth);
provide('userManager', user);
provide('notionManager', notion);
provide('githubManager', github);

onBeforeMount(async () => {
  await user.getMe();
});
</script>

<template>
  <div>
    <CNotionSetting class="mb-10" />
    <CGithubSetting class="mb-10" />
    <CActionSetting class="mb-10" />
    <CNoticeSetting class="mb-10" />
    <CDangerZoneSetting />
  </div>
</template>
