<script setup>
import CSettingTitle from '@/views/user/setting/components/CSettingTitle.vue';
import SConnectionStatus from '@/shared/common/SConnectionStatus.vue';
import SActionButton from '@/shared/buttons/SActionButton.vue';
import CSettingSubTitle from '@/views/user/setting/components/CSettingSubTitle.vue';
import SInformationHint from '@/shared/hints/SInformationHint.vue';
import { inject, onMounted, ref, watch } from 'vue';
import SSimpleTextInput from '@/shared/input/SSimpleTextInput.vue';
import SSpinnerInner from '@/shared/spinner/SSpinnerInner.vue';
import SWarningHint from '@/shared/hints/SWarningHint.vue';

const user = inject('userManager');
const githubInfoFrom = ref({
  repositoryName: '',
  owner: '',
  email: ''
});

watch(user.info, (info) => {
  githubInfoFrom.value.repositoryName = info.githubRepository;
  githubInfoFrom.value.owner = info.githubOwner;
  githubInfoFrom.value.email = info.githubEmail;
});

onMounted(() => {
  getGithubInfo();
});

const getGithubInfo = () => {
  user.getConnectedGithubInfo();
};

const bindRepositoryName = (repository) => {
  githubInfoFrom.value.repositoryName = repository.name;
};

const checkConnectedGithub = () => {
  user.getConnectedGithubInfo();
  user.getInfo();
};
</script>

<template>
  <div>
    <CSettingTitle title="Github" />
    <ul class="border border-main p-4">
      <!--      connected -->
      <li class="flex justify-between border-b border-main pb-4 items-center">
        <div>
          <SConnectionStatus
            :isConnected="user.githubInfo.value?.isGithubValid"
          />
          <SWarningHint
            class="mt-1 text-danger"
            v-if="!user.githubInfo.value?.isGithubValid"
            text="Github정보와 아래정보가 동일한지 확인해주세요."
          />
        </div>
        <SActionButton name="연결 확인" @action="checkConnectedGithub" />
      </li>

      <!--          repository -->
      <li class="border-b border-main py-4">
        <CSettingSubTitle title="Repository name" />
        <div class="sm:flex sm:justify-between sm:items-center sm:mb-4">
          <SSimpleTextInput
            v-model="githubInfoFrom.repositoryName"
            class="sm:w-[50%]"
            placeholder="Repository Name"
            :class="{
              'border-2 border-danger rounded-md outline-none':
                !user.githubInfo.value.isGithubRepositoryValid
            }"
          />
          <SActionButton
            class="sm:w-[50%] text-right mt-1 sm:mt-0"
            name="저장"
            @action="user.saveRepositoryName(githubInfoFrom.repositoryName)"
          />
        </div>

        <div class="mt-8 flex items-center gap-2">
          <CSettingSubTitle title="Github Public Repository" />
          <span class="text-xs font-noto_sans_m">
            {{ user.githubInfo.value?.githubRepositories?.length }}
          </span>
        </div>
        <SInformationHint
          class="mb-2"
          text="기존 Git Repository를 NOGI와 연결할 수 있어요"
        />
        <ul
          class="max-h-40 overflow-y-scroll"
          v-if="user.githubInfo.value.githubRepositories"
        >
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
        <div class="h-40" v-else>
          <SSpinnerInner />
        </div>
      </li>

      <!--          owner -->
      <li class="border-b border-main py-4">
        <CSettingSubTitle title="Owner" />
        <div class="sm:flex sm:justify-between sm:items-center">
          <SSimpleTextInput
            v-model="githubInfoFrom.owner"
            class="sm:w-[50%]"
            placeholder="NOGI Owner"
            :class="{
              'border-2 border-danger rounded-md outline-none':
                !user.githubInfo.value.isGithubOwnerValid
            }"
          />
          <SActionButton
            class="sm:w-[50%] text-right mt-1 sm:mt-0"
            name="저장"
            @action="user.saveGithubOwner(githubInfoFrom.owner)"
          />
        </div>
      </li>

      <!--          email -->
      <li class="py-4">
        <CSettingSubTitle title="Email" />
        <div class="sm:flex sm:justify-between sm:items-center">
          <SSimpleTextInput
            v-model="githubInfoFrom.email"
            class="sm:w-[50%]"
            placeholder="NOGI Email"
            :class="{
              'border-2 border-danger rounded-md outline-none':
                !user.githubInfo.value.isGithubEmailValid
            }"
          />
          <SActionButton
            class="sm:w-[50%] text-right mt-1 sm:mt-0"
            name="저장"
            @action="user.saveGithubEmail(githubInfoFrom.email)"
          />
        </div>
      </li>
    </ul>
  </div>
</template>
