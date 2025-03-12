<script setup>
import CSettingTitle from '@/views/user/setting/components/CSettingTitle.vue';
import ConnectionStatus from '@/shared/common/ConnectionStatus.vue';
import ActionButton from '@/shared/buttons/ActionButton.vue';
import CSettingSubTitle from '@/views/user/setting/components/CSettingSubTitle.vue';
import InformationText from '@/shared/common/InformationText.vue';
import { inject, watch } from 'vue';

const user = inject('userManager');
const props = defineProps({
  userInfo: {
    type: Object,
    default: () => {}
  }
});

watch(user.info, (value) => {
  console.log('user info value :: ', value);
});

const bindRepositoryName = (repository) => {
  console.log('repository => ', repository);
  // nogiGithubInfo.value.repositoryName =
};
</script>

<template>
  <div>
    <CSettingTitle title="Github" />
    <ul class="border border-main p-4">
      <!--      connected -->
      <li class="flex justify-between border-b border-main pb-4 items-center">
        <ConnectionStatus :isConnected="user.githubInfo.value?.isGithubValid" />
        <ActionButton
          name="연결 확인"
          @action="user.getConnectedGithubInfo()"
        />
      </li>

      <!--          repository -->
      <li class="border-b border-main py-4">
        <CSettingSubTitle title="Repository name" />
        <div class="sm:flex sm:justify-between sm:items-center sm:mb-4">
          <!--          <SimpleTextInput-->
          <!--            v-model=".value.repositoryName"-->
          <!--            class="sm:w-[50%]"-->
          <!--            placeholder="Repository Name"-->
          <!--          />-->
          <!--          <ActionButton-->
          <!--            class="sm:w-[50%] text-right mt-1 sm:mt-0"-->
          <!--            name="저장"-->
          <!--            @action="saveRepositoryName"-->
          <!--          />-->
        </div>

        <div class="mt-8 flex items-center gap-2">
          <CSettingSubTitle title="Github Public Repository" />
          <span class="text-xs font-noto_sans_m">
            {{ user.githubInfo.value?.githubRepositories?.length }}
          </span>
        </div>
        <InformationText
          class="mb-2"
          text="기존 Git Public Repository를 선택하여 NOGI와 연결할 수 있어요"
        />
        <ul class="max-h-40 overflow-scroll">
          <li
            v-for="(item, index) in user.githubInfo.value?.githubRepositories"
            :key="index"
            class="sm:flex sm:gap-5 mb-3"
          >
            <p
              class="text-xs sm:text-base cursor-pointer text-neutral"
              @click="bindRepositoryName(item)"
            >
              {{ item.name }}
            </p>
            <a
              :href="item.htmlUrl"
              class="text-action hover:underline break-all text-xs sm:text-base"
              rel="noopener noreferrer"
              target="_blank"
            >
              <font-awesome-icon class="text-sm" icon="fa-solid fa-link" />
              {{ item.htmlUrl }}
            </a>
          </li>
        </ul>
      </li>

      <!--          owner -->
      <li class="border-b border-main py-4">
        <CSettingSubTitle title="Owner" />
        <div class="sm:flex sm:justify-between sm:items-center">
          <!--          <SimpleTextInput-->
          <!--            v-model="nogiGithubInfo.owner"-->
          <!--            class="sm:w-[50%]"-->
          <!--            placeholder="NOGI Owner"-->
          <!--          />-->
          <!--          <ActionButton-->
          <!--            class="sm:w-[50%] text-right mt-1 sm:mt-0"-->
          <!--            name="저장"-->
          <!--            @action="createNewNotionDatabase"-->
          <!--          />-->
        </div>
      </li>

      <!--          email -->
      <li class="py-4">
        <CSettingSubTitle title="Email" />
        <div class="sm:flex sm:justify-between sm:items-center">
          <!--          <SimpleTextInput-->
          <!--            v-model="nogiGithubInfo.email"-->
          <!--            class="sm:w-[50%]"-->
          <!--            placeholder="NOGI Email"-->
          <!--          />-->
          <!--          <ActionButton-->
          <!--            class="sm:w-[50%] text-right mt-1 sm:mt-0"-->
          <!--            name="저장"-->
          <!--            @action="createNewNotionDatabase"-->
          <!--          />-->
        </div>
      </li>
    </ul>
  </div>
</template>

<style scoped></style>
