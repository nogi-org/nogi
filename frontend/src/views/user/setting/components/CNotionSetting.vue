<script setup>
import SInformationHint from '@/shared/hints/SInformationHint.vue';
import SConnectionStatus from '@/shared/common/SConnectionStatus.vue';
import SActionButton from '@/shared/buttons/SActionButton.vue';
import CSettingTitle from '@/views/user/setting/components/CSettingTitle.vue';
import { inject, onMounted } from 'vue';
import { NotionManager } from '@/manager/notion/NotionManager.js';

const notion = new NotionManager();
const auth = inject('authManager');
const user = inject('userManager');

onMounted(() => {
  checkNotionConnected();
});

const checkNotionConnected = () => {
  user.getConnectedNotion();
};
</script>

<template>
  <div>
    <CSettingTitle title="Notion" />
    <div class="border border-main p-4">
      <div class="flex justify-between">
        <SConnectionStatus :isConnected="user.notionConnected.value" />
        <div class="flex gap-2">
          <SActionButton
            name="New연결"
            @action="auth.toNotionPageForCreateNewDatabase()"
          />
          <SActionButton
            name="연결 확인"
            @action="notion.onDatabaseConnectionTest()"
          />
        </div>
      </div>
      <SInformationHint
        text="New연결 버튼으로 새로운 노션 Database를 생성할 수 있어요"
        class="mt-1"
      />
    </div>
  </div>
</template>
