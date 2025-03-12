<script setup>
import CSettingTitle from '@/views/user/setting/components/CSettingTitle.vue';
import { inject } from 'vue';
import OnOffToggleButton from '@/shared/buttons/OnOffToggleButton.vue';

const user = inject('userManager');
</script>

<template>
  <div>
    <div>
      <CSettingTitle title="Notice" />
      <ul class="border border-main p-4">
        <li class="flex items-center justify-between gap-3">
          <span class="text-sm">
            GitHub 전송 성공 여부를 Issue로 알림 받기
          </span>
          <OnOffToggleButton
            :isOn="user.info.value.isNotificationAllowed"
            @action="user.updateNotificationAllow()"
          />
        </li>
      </ul>
    </div>
  </div>
</template>
