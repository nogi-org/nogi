<script setup>
import SSimpleTextCallout from '@/shared/callout/SSimpleTextCallout.vue';
import SConnectionStatus from '@/shared/common/SConnectionStatus.vue';
import SActionButton from '@/shared/buttons/SActionButton.vue';
import SMainTitle from '@/shared/title/SMainTitle.vue';
import { inject, onMounted } from 'vue';

const auth = inject('authManager');
const notion = inject('notionManager');

onMounted(() => {
  notion.getConnectedNotion();
});
</script>

<template>
  <div>
    <SMainTitle title="Notion" />
    <div class="border border-main p-4">
      <div class="flex justify-between">
        <SConnectionStatus :isConnected="notion.connection.value" />
        <div class="flex gap-2">
          <SActionButton
            name="New연결"
            @action="auth.toNotionPageForCreateNewDatabase()"
          />
          <SActionButton
            name="연결 확인"
            @action="notion.getConnectedNotion()"
          />
        </div>
      </div>
      <SSimpleTextCallout
        class="mt-1"
        text="New연결 버튼으로 새로운 노션 Database를 생성할 수 있어요"
      />
    </div>
  </div>
</template>
