<script setup>
import CSettingTitle from '@/views/user/setting/components/CSettingTitle.vue';
import SInformationHint from '@/shared/hints/SInformationHint.vue';
</script>

<template>
  <div>
    <CSettingTitle title="Action" />
    <ul class="border border-main p-4">
      <li>
        <div class="flex justify-between items-center mb-2">
          <span class="text-sm">수동실행</span>
          <!--              <ActionButton name="Action" @click="onManual" />-->
        </div>
        <SInformationHint
          text="버튼을 클릭하면 NOGI의 자동 실행 주기를 기다릴 필요 없이, 즉시 실행할 수 있어요."
        />
      </li>
    </ul>
  </div>
</template>
