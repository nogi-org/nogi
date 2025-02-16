<script setup>
import { onBeforeMount, ref } from 'vue';
import { UserManager } from '@/manager/user/UserManager.js';
import Validation from '@/components/common/Validation.vue';

const user = new UserManager();
const validation = ref({});
const userInfo = ref({});

onBeforeMount(async () => {
  await getUserInfo();
});

const getUserInfo = async () => {
  userInfo.value = await user.getUserInfo();
  console.log('userInfo.value : ', userInfo.value);
};

const updateUserInfo = async () => {
  // todo: 유효성검사를 매니저에 넣는 방법으로 생각해보기
  const check = user.checkInfoValidation(userInfo.value);
  if (!check.isResult) {
    validation.value = check.result;
    return;
  }
  await user.updateUserInfo();
  validation.value = {};
  await getUserInfo();
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
      <!--      notion auth Key-->
      <li class="mb-5">
        <!--        todo: label, input, validation component 로 분리-->
        <label
          for=""
          class="relative text-gray-500 text-sm font-noto_sans_b after:content-['*'] after:text-danger after:absolute after:-right-2.5 after:top-[65%] after:-translate-y-1/2"
        >
          Notion Bot Token
        </label>
        <input
          v-model="userInfo.notionAuthToken"
          class="border-main border text-xs shadow-sm w-full rounded-md px-3 h-12 tracking-wider"
          :class="{ 'ring-danger ring-1': validation.notionAuthToken }"
          placeholder="Notion Auth Token을 입력해주세요."
        />
        <Validation
          v-if="validation?.notionAuthToken"
          :notice="validation?.notionAuthToken"
          class="mt-1"
        />
      </li>

      <!--      notion database key-->
      <li class="mb-5">
        <!--        todo: label, input, validation component 로 분리-->
        <label
          for=""
          class="relative text-gray-500 text-sm font-noto_sans_b after:content-['*'] after:text-danger after:absolute after:-right-2.5 after:top-[65%] after:-translate-y-1/2"
        >
          Notion Database Id
        </label>
        <input
          v-model="userInfo.notionDatabaseId"
          class="border-main border text-xs shadow-sm w-full rounded-md px-3 h-12 tracking-wider"
          :class="{ 'ring-danger ring-1': validation.notionDatabaseId }"
          placeholder="Notion Database Id를 입력해주세요."
        />
        <Validation
          v-if="validation?.notionDatabaseId"
          :notice="validation?.notionDatabaseId"
          class="mt-1"
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
