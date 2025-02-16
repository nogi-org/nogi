<script setup>
import { onBeforeMount, ref } from 'vue';
import { UserManager } from '@/manager/user/UserManager.js';
import Validation from '@/components/common/Validation.vue';

const user = new UserManager();
const validation = ref(null);
const currentCategoryId = ref(0);

onBeforeMount(async () => {
  await loadPosts(currentCategoryId.value);
});

const loadPosts = async categoryId => {};

const setInput = (value, target) => {
  switch (target) {
    case 'token':
      user.setNotionBotToken(value);
      break;
    case 'databaseKey':
      user.setDatabaseKey(value);
      break;
  }
};

const registerUserInfo = () => {
  const infoValidation = user.checkInfoValidation();
  validation.value = infoValidation.result;
  if (!infoValidation.isResult) {
    return;
  }
  console.log('TODO: 등록 작업 필요');
};
</script>

<template>
  <div>
    <div class="border border-main rounded-md p-4 mb-10">
      <button
        class="mb-3 bg-action text-xs px-3 py-2 tracking-widest font-medium rounded-md sm:px-2.5"
      >
        수동실행
      </button>
      <div class="text-xs sm:text-sm text-gray-500">
        <font-awesome-icon
          icon="fa-solid fa-circle-info"
          class="text-gray-500 mr-1 text-sm"
        />
        <span>
          버튼을 클릭하면 NOGI의 자동 실행 주기를 기다릴 필요 없이, 원하는
          시점에 즉시 수동으로 실행할 수 있어요.
        </span>
      </div>
    </div>

    <ul>
      <li class="mb-5">
        <!--        todo: label, input, validation component 로 분리-->
        <label
          for=""
          class="relative text-gray-500 text-sm font-noto_sans_b after:content-['*'] after:text-danger after:absolute after:-right-2.5 after:top-[65%] after:-translate-y-1/2"
        >
          Notion Bot Token
        </label>
        <input
          @input="setInput($event.target.value, 'token')"
          class="border-main border text-xs shadow-sm w-full rounded-md px-3 h-12 tracking-wider"
          :class="{ 'ring-danger ring-1': validation?.notionBotToken }"
          placeholder="Notion Bot Token을 입력해주세요."
        />
        <Validation
          v-if="validation?.notionBotToken"
          :notice="validation?.notionBotToken"
          class="mt-1"
        />
      </li>
      <li class="mb-5">
        <!--        todo: label, input, validation component 로 분리-->
        <label
          for=""
          class="relative text-gray-500 text-sm font-noto_sans_b after:content-['*'] after:text-danger after:absolute after:-right-2.5 after:top-[65%] after:-translate-y-1/2"
        >
          Notion Database Key
        </label>
        <input
          @input="setInput($event.target.value, 'databaseKey')"
          class="border-main border text-xs shadow-sm w-full rounded-md px-3 h-12 tracking-wider"
          :class="{ 'ring-danger ring-1': validation?.databaseKey }"
          placeholder="Notion Database Key를 입력해주세요."
        />
        <Validation
          v-if="validation?.databaseKey"
          :notice="validation?.databaseKey"
          class="mt-1"
        />
      </li>
      <li class="flex justify-end">
        <button
          @click="registerUserInfo"
          class="mb-3 bg-action text-xs px-3 py-2 tracking-widest font-medium rounded-md sm:px-2.5"
        >
          저장
        </button>
        <!--        <button-->
        <!--          class="mb-3 bg-warning text-xs px-3 py-2 tracking-widest font-medium rounded-md sm:px-2.5"-->
        <!--        >-->
        <!--          수정-->
        <!--        </button>-->
      </li>
    </ul>
  </div>
</template>
