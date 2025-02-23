<script setup>
import { onMounted, watch } from 'vue';
import { UserManager } from '@/manager/user/UserManager.js';
import Validation from '@/components/common/Validation.vue';

const user = new UserManager();
const userInfo = user.info;
const validation = user.infoUpdateValidation;

onMounted(async () => {
  await user.getInfo();
});

watch(
  () => userInfo.value.githubRepository,
  newValue => {
    user.watchRepositoryName(newValue);
  }
);

const updateUserInfo = async () => {
  user.checkInfoValidation();
  if (!validation.value.isResult) {
    return;
  }
  await user.updateInfo();
};

const checkRepositoryName = async () => {
  await user.checkRepositoryName();
};

const onManualNogi = async () => {
  await user.onManualNogi();
};
</script>

<template>
  <div>
    <div class="border border-main rounded-md p-4 mb-10">
      <button
        class="mb-3 bg-action text-xs px-3 py-2 tracking-widest font-medium rounded-md sm:px-2.5"
        @click="onManualNogi"
      >
        수동실행
      </button>
      <div class="text-xs sm:text-sm text-neutral">
        <font-awesome-icon
          icon="fa-solid fa-circle-info"
          class="text-neutral mr-1 text-sm"
        />
        <span>
          버튼을 클릭하면 NOGI의 자동 실행 주기를 기다릴 필요 없이, 즉시 실행할
          수 있어요.
        </span>
      </div>
    </div>

    <ul>
      <!--      notion auth Key-->
      <li class="mb-5">
        <!--        todo: label, input, validation component 로 분리-->
        <label
          for=""
          class="relative text-neutral text-sm font-noto_sans_b after:content-['*'] after:text-danger after:absolute after:-right-2.5 after:top-[65%] after:-translate-y-1/2"
        >
          Notion Bot Token
        </label>
        <input
          v-model="userInfo.notionBotToken"
          class="border-main border text-xs shadow-sm w-full rounded-md px-3 h-12 tracking-wider"
          :class="{ 'ring-danger ring-1': validation?.result?.notionBotToken }"
          placeholder="Notion Bot Token을 입력해주세요."
        />
        {{ validation?.result?.notionBotToken }}
        <Validation
          v-if="validation?.result?.notionBotToken"
          :notice="validation?.result?.notionBotToken"
          class="mt-1"
        />
      </li>

      <!--      notion database key-->
      <li class="mb-5">
        <!--        todo: label, input, validation component 로 분리-->
        <label
          for=""
          class="relative text-neutral text-sm font-noto_sans_b after:content-['*'] after:text-danger after:absolute after:-right-2.5 after:top-[65%] after:-translate-y-1/2"
        >
          Notion Database ID
        </label>
        <input
          v-model="userInfo.notionDatabaseId"
          class="border-main border text-xs shadow-sm w-full rounded-md px-3 h-12 tracking-wider"
          :class="{
            'ring-danger ring-1': validation?.result?.notionDatabaseId
          }"
          placeholder="Notion Database Id를 입력해주세요."
        />
        <Validation
          v-if="validation?.result?.notionDatabaseId"
          :notice="validation?.result?.notionDatabaseId"
          class="mt-1"
        />
      </li>

      <li class="mb-5">
        <label
          for=""
          class="relative text-neutral text-sm font-noto_sans_b after:content-['*'] after:text-danger after:absolute after:-right-2.5 after:top-[65%] after:-translate-y-1/2"
        >
          Github Repository Name
        </label>
        <input
          v-model="userInfo.githubRepository"
          class="border-main border text-xs shadow-sm w-full rounded-md px-3 h-12 tracking-wider"
          :class="{
            'ring-danger ring-1': validation?.result?.githubRepository
          }"
          placeholder="TIL을 기록할 멋진 Repository 이름을 작성해주세요."
        />
        <Validation
          v-if="validation?.result?.githubRepository"
          :notice="validation?.result?.githubRepository"
          class="mt-1"
        />
        <div class="text-right mt-1">
          <button
            class="bg-action text-xs px-2.5 py-1 rounded-md sm:text-sm"
            @click="checkRepositoryName"
          >
            확인
          </button>
        </div>
      </li>

      <li class="mb-5">
        <label for="" class="relative text-neutral text-sm font-noto_sans_b">
          Github Email
        </label>
        <input
          v-model="userInfo.githubEmail"
          readonly
          class="border-main border text-xs shadow-sm w-full rounded-md px-3 h-12 tracking-wider text-neutral"
        />
      </li>

      <li class="flex justify-end">
        <button
          @click="updateUserInfo"
          class="mb-3 bg-action text-xs px-3 py-2 tracking-widest font-medium rounded-md sm:px-2.5"
        >
          저장
        </button>
      </li>
    </ul>
  </div>
</template>
