<script setup>
import { onBeforeMount, provide } from 'vue';
import { UserManager } from '@/manager/user/UserManager.js';
import CNotionSetting from '@/views/user/setting/components/CNotionSetting.vue';
import CGithubSetting from '@/views/user/setting/components/CGithubSetting.vue';
import CActionSetting from '@/views/user/setting/components/CActionSetting.vue';
import CNoticeSetting from '@/views/user/setting/components/CNoticeSetting.vue';
import CDangerZoneSetting from '@/views/user/setting/components/CDangerSetting.vue';

const user = new UserManager();
provide('userManager', user);

onBeforeMount(async () => {
  await user.getReturnInfo();
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
