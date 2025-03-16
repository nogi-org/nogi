<script setup>
import CSettingTitle from '@/views/user/setting/components/CSettingTitle.vue';
import { AdminManager } from '@/manager/admin/AdminManager.js';

const admin = new AdminManager();
</script>

<template>
  <div>
    <CSettingTitle title="Action" />
    <div class="border border-main p-4">
      <button
        class="bg-action px-3 py-1 rounded-md"
        @click="admin.updateNotionPageIdOfAllUser()"
      >
        모든 유저 Notion Page ID 업데이트
      </button>
    </div>
  </div>
</template>
