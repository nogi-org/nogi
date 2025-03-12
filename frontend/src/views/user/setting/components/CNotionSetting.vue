<script setup>
import InformationText from '@/shared/common/InformationText.vue';
import ConnectionStatus from '@/shared/common/ConnectionStatus.vue';
import ActionButton from '@/shared/buttons/ActionButton.vue';
import CSettingTitle from '@/views/user/setting/components/CSettingTitle.vue';
import { inject } from 'vue';

const user = inject('userManager');
const createNewNotionDatabase = () => {
  console.log('createNewNotionDatabase');
};
</script>

<template>
  <div>
    <CSettingTitle title="Notion" />
    <div class="border border-main p-4">
      <div class="flex justify-between">
        <ConnectionStatus :isConnected="true" />
        <div class="flex gap-2">
          <ActionButton name="새로 만들기" @action="createNewNotionDatabase" />
          <ActionButton name="연결 확인" @action="createNewNotionDatabase" />
        </div>
      </div>
      <InformationText
        text="새로 만들기 버튼으로 새로운 노션 Database를 생성할 수 있어요"
      />
    </div>
  </div>
</template>
