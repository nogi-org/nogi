<script setup>
import {onMounted} from 'vue';
import {UserManager} from '@/manager/user/UserManager.js';
import Validation from '@/components/common/Validation.vue';
import {NotionManager} from '@/manager/notion/NotionManager.js';

const user = new UserManager();
const notion = new NotionManager();

onMounted(async () => {
  await user.getInfo();
});

// 알림 여부 토글
const toggleNotification = () => {
  user.info.value.isNotificationAllowed =
      !user.info.value.isNotificationAllowed;
};

const updateUserInfo = async () => {
  const validationResult = user.checkUpdateInfoValidation();
  if (!validationResult.value.isSuccess) {
    return;
  }
  await user.updateInfo();
};

const checkRepositoryName = async () => {
  await user.checkRepositoryName();
};

const onManual = async () => {
  await user.onManual();
};

const checkNotionDatabaseConnectionTest = async () => {
  const response = await notion.onDatabaseConnectionTest(
      user.info.value.notionBotToken,
      user.info.value.notionDatabaseId
  );
  if (!response.isSuccess) {
    return;
  }
  user.isSuccessNotionDatabaseConnectionTest.value = true;
  user.info.value.notionBotToken = response.botToken;
  user.info.value.notionDatabaseId = response.databaseId;
  user.initInfoUpdateValidation();
};
</script>

<template>
  <div>
    <div class="border border-main rounded-md p-4 mb-10">
      <button
          class="mb-3 bg-action text-xs px-3 py-2 tracking-widest font-medium rounded-md sm:px-2.5"
          @click="onManual"
      >
        수동실행
      </button>
      <div class="text-xs sm:text-sm text-neutral">
        <font-awesome-icon
            class="text-neutral mr-1 text-sm"
            icon="fa-solid fa-circle-info"
        />
        <span>
          버튼을 클릭하면 NOGI의 자동 실행 주기를 기다릴 필요 없이, 즉시 실행할
          수 있어요.
        </span>
      </div>
    </div>

    <ul>
      <!--      notion auth Key-->
      <!-- Notion Bot Token -->
      <li class="mb-5">
        <label
            class="relative text-neutral text-sm font-noto_sans_b after:content-['*'] after:text-danger"
        >
          Notion Bot Token
        </label>

        <!-- 마스킹된 값 (연결 확인 성공 후) -->
        <input
            v-if="user.isSuccessNotionDatabaseConnectionTest.value"
            class="border-main border text-xs shadow-sm w-full rounded-md px-3 h-12 tracking-wider text-neutral"
            readonly
            value="*****************************************"
        />

        <!-- 원래 입력값 (연결 확인 전) -->
        <input
            v-else
            v-model="user.info.value.notionBotToken"
            class="border-main border text-xs shadow-sm w-full rounded-md px-3 h-12 tracking-wider"
            placeholder="Notion Bot Token을 입력해주세요."
        />

        <Validation
            v-if="user.infoUpdateValidation.value?.result?.notionBotToken"
            :notice="user.infoUpdateValidation.value?.result?.notionBotToken"
            class="mt-1"
        />
      </li>

      <!-- Notion Database ID -->
      <li class="mb-5">
        <label
            class="relative text-neutral text-sm font-noto_sans_b after:content-['*'] after:text-danger"
        >
          Notion Database ID
        </label>

        <!-- 마스킹된 값 (연결 확인 성공 후) -->
        <input
            v-if="user.isSuccessNotionDatabaseConnectionTest.value"
            class="border-main border text-xs shadow-sm w-full rounded-md px-3 h-12 tracking-wider text-neutral"
            readonly
            value="*****************************************"
        />

        <!-- 원래 입력값 (연결 확인 전) -->
        <input
            v-else
            v-model="user.info.value.notionDatabaseId"
            class="border-main border text-xs shadow-sm w-full rounded-md px-3 h-12 tracking-wider"
            placeholder="Notion Database Id를 입력해주세요."
        />

        <Validation
            v-if="user.infoUpdateValidation.value?.result?.notionDatabaseId"
            :notice="user.infoUpdateValidation.value?.result?.notionDatabaseId"
            class="mt-1"
        />
      </li>
      <div class="text-right mt-1">
        <button
            v-if="!user.isSuccessNotionDatabaseConnectionTest.value"
            class="bg-action text-xs px-2.5 py-1 rounded-md sm:text-sm"
            @click="checkNotionDatabaseConnectionTest"
        >
          연결 확인
        </button>
        <button
            v-if="user.isSuccessNotionDatabaseConnectionTest.value"
            class="bg-warning text-xs px-2.5 py-1 rounded-md sm:text-sm"
            @click="
            () => {
              user.isSuccessNotionDatabaseConnectionTest.value = false;
              const originNotionDatabaseId = user.info.value.notionDatabaseId;
              user.info.value.notionDatabaseId = `https://www.notion.so/${originNotionDatabaseId}?v=xxx&pvs=x`;
            }
          "
        >
          수정
        </button>
      </div>

      <!-- Github Repository Name -->
      <li class="mb-5">
        <label
            class="relative text-neutral text-sm font-noto_sans_b after:content-['*'] after:text-danger after:absolute after:-right-2.5 after:top-[65%] after:-translate-y-1/2"
            for=""
        >
          Github Repository Name
        </label>
        <input
            v-model="user.info.value.githubRepository"
            :class="[
            {
              'ring-danger ring-1':
                user.infoUpdateValidation.value?.result?.githubRepository
            },
            { 'text-neutral': user.isSuccessRepositoryNameCheck.value }
          ]"
            :readonly="user.isSuccessRepositoryNameCheck.value"
            class="border-main border text-xs shadow-sm w-full rounded-md px-3 h-12 tracking-wider"
            placeholder="TIL을 기록할 멋진 Repository 이름을 작성해주세요."
        />
        <Validation
            v-if="user.infoUpdateValidation.value?.result?.githubRepository"
            :notice="user.infoUpdateValidation.value?.result?.githubRepository"
            class="mt-1"
        />
        <div class="text-right mt-1">
          <button
              v-if="!user.isSuccessRepositoryNameCheck.value"
              class="bg-action text-xs px-2.5 py-1 rounded-md sm:text-sm"
              @click="checkRepositoryName"
          >
            확인
          </button>
          <button
              v-if="user.isSuccessRepositoryNameCheck.value"
              class="bg-warning text-xs px-2.5 py-1 rounded-md sm:text-sm"
              @click="user.isSuccessRepositoryNameCheck.value = false"
          >
            수정
          </button>
        </div>
      </li>

      <li class="mb-5">
        <label class="relative text-neutral text-sm font-noto_sans_b" for="">
          Github Email
        </label>
        <input
            v-model="user.info.value.githubEmail"
            class="border-main border text-xs shadow-sm w-full rounded-md px-3 h-12 tracking-wider text-neutral"
            readonly
        />
      </li>

      <!-- 알림 동의 여부 (isNotificationAllowed) -->
      <li class="mb-5 flex items-center justify-start gap-3">
        <label class="text-neutral text-sm font-noto_sans_b">
          GitHub 전송 성공 여부를 Issue로 알림 받기
        </label>

        <!-- Toggle Switch -->
        <button
            :class="{
            'bg-green-500': user.info.value.isNotificationAllowed,
            'bg-gray-300': !user.info.value.isNotificationAllowed
          }"
            class="relative w-10 h-6 flex items-center border border-main rounded-full transition-colors ms-auto"
            @click="toggleNotification"
        >
          <span
              :class="{
              'translate-x-4': user.info.value.isNotificationAllowed
            }"
              class="absolute left-0.5 w-4 h-4 bg-white rounded-full shadow-md transition-transform"
          ></span>
        </button>
      </li>

      <li class="border-t border-gray-600 pt-5 flex justify-end">
        <button
            class="bg-action text-xs px-3 py-2 tracking-widest font-medium rounded-md sm:px-2.5"
            @click="updateUserInfo"
        >
          저장
        </button>
      </li>
    </ul>
  </div>
</template>
