<script setup>
import CSettingTitle from '@/views/setting/components/CSettingTitle.vue';
import SInformationHint from '@/shared/hints/SInformationHint.vue';
import SActionButton from '@/shared/buttons/SActionButton.vue';
import { inject } from 'vue';

const user = inject('userManager');
const notion = inject('notionManager');
const github = inject('githubManager');

const onManualSync = () => {
  const isConnectedNotion = notion.connection.value;
  const isConnectedGithub = github.connection.value?.isGithubValid;
  user.onManualAsync(isConnectedNotion, isConnectedGithub);
};
</script>

<template>
  <div>
    <CSettingTitle title="Action" />
    <ul class="border border-main p-4">
      <li>
        <div class="flex justify-between items-center mb-2">
          <span class="text-sm">수동실행</span>
          <SActionButton name="Action" @click="onManualSync" />
        </div>
        <SInformationHint
          text="버튼을 클릭하면 NOGI의 자동 실행 주기를 기다릴 필요 없이, 즉시 실행할 수 있어요."
        />
      </li>
    </ul>
  </div>
</template>
