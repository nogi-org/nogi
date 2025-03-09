<script setup>
import { onMounted, ref } from 'vue';
import { UserManager } from '@/manager/user/UserManager.js';
import Validation from '@/components/common/Validation.vue';
import { NotionManager } from '@/manager/notion/NotionManager.js';
import InformationText from '@/components/common/InformationText.vue';
import ConfirmModal from '@/components/modal/ConfirmModal.vue';
import ConnectionStatus from '@/components/common/ConnectionStatus.vue';
import OnOffToggleButton from '@/components/buttons/OnOffToggleButton.vue';
import ActionButton from '@/components/buttons/ActionButton.vue';
import DangerButton from '@/components/buttons/DangerButton.vue';
import SimpleTextInput from '@/components/input/SimpleTextInput.vue';

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
const createNewNotionDatabase = () => {
  console.log('노션 데이터베이스 새로 생성! : ');
};

const repositoryName = 'superpil';
</script>

<template>
  <ul>
    <!--    notion -->
    <li class="mb-10">
      <h2 class="font-noto_sans_b text-lg">Notion</h2>
      <hr class="border-main my-2" />
      <ul class="border border-main p-4">
        <li>
          <div class="flex justify-between">
            <ConnectionStatus :isConnected="true" />
            <div class="flex gap-2">
              <ActionButton
                name="새로 만들기"
                @action="createNewNotionDatabase"
              />
              <ActionButton
                name="연결 확인"
                @action="createNewNotionDatabase"
              />
            </div>
          </div>
          <InformationText
            text="새로 만들기 버튼으로 새로운 노션 Database를 생성할 수 있어요"
          />
        </li>
      </ul>
    </li>

    <!--    github-->
    <li class="mb-10">
      <h2 class="font-noto_sans_b text-lg">Github</h2>
      <hr class="border-main my-2" />
      <ul class="border border-main p-4">
        <li class="flex justify-between border-b border-main pb-4 items-center">
          <ConnectionStatus />
          <ActionButton name="연결 확인" @action="createNewNotionDatabase" />
        </li>
        <li class="border-b border-main py-4">
          <h3 class="font-noto_sans_m mb-2">Repository name</h3>
          <div class="flex justify-between items-center mb-4">
            <SimpleTextInput
              v-model="repositoryName"
              placeholder="Repository Name"
              class="w-[50%]"
            />
            <ActionButton
              name="저장"
              @action="createNewNotionDatabase"
              class="w-[50%] text-right"
            />
          </div>
          <ul class="mt-9">
            <li class="font-noto_sans_m mb-3">
              <h3>Github Public Repository</h3>
              <InformationText
                text="현재 생성된 Git Public Repository에서 선택하여 NOGI와 연결할 수 있어요"
              />
            </li>
            <li class="sm:flex sm:gap-5 mb-3">
              <p
                class="cursor-pointer text-neutral"
                @click="repositoryName = 'DEMO3'"
              >
                nogi-til-prod
              </p>
              <a
                href="https://github.com/chatgptisgod/nogi-til-prod"
                target="_blank"
                rel="noopener noreferrer"
                class="text-action hover:underline break-all"
              >
                <font-awesome-icon icon="fa-solid fa-link" class="text-sm" />
                https://github.com/chatgptisgod/nogi-til-prod
              </a>
            </li>
            <li class="sm:flex sm:gap-5 mb-3">
              <p
                class="cursor-pointer text-neutral"
                @click="repositoryName = 'DEMO3'"
              >
                nogi-til-prod
              </p>
              <a
                href="https://github.com/chatgptisgod/nogi-til-prod"
                target="_blank"
                rel="noopener noreferrer"
                class="text-action hover:underline break-all"
              >
                <font-awesome-icon icon="fa-solid fa-link" class="text-sm" />
                https://github.com/chatgptisgod/nogi-til-prod
              </a>
            </li>
          </ul>
        </li>
        <li class="border-b border-main py-4">
          <h3 class="font-noto_sans_m mb-2">Owner</h3>
          <SimpleTextInput
            v-model="repositoryName"
            placeholder="Git Owner"
            class="w-[50%] mb-1"
            :disabled="true"
          />
          <div class="flex justify-between items-center">
            <SimpleTextInput
              v-model="repositoryName"
              placeholder="NOGI Owner"
              class="w-[50%]"
            />
            <ActionButton
              name="저장"
              @action="createNewNotionDatabase"
              class="w-[50%] text-right"
            />
          </div>
        </li>
        <li class="py-4">
          <h3 class="font-noto_sans_m mb-2">Email</h3>
          <SimpleTextInput
            v-model="repositoryName"
            placeholder="Git Email"
            class="w-[50%] mb-1"
            :disabled="true"
          />
          <div class="flex justify-between items-center">
            <SimpleTextInput
              v-model="repositoryName"
              placeholder="NOGI Email"
              class="w-[50%]"
            />
            <ActionButton
              name="저장"
              @action="createNewNotionDatabase"
              class="w-[50%] text-right"
            />
          </div>
        </li>
      </ul>
    </li>

    <!--    action-->
    <li class="mb-10">
      <h2 class="font-noto_sans_b text-lg">Action</h2>
      <hr class="border-main my-2" />
      <ul class="border border-main p-4">
        <li>
          <div class="flex justify-between items-center mb-2">
            <span class="text-sm">수동실행</span>
            <ActionButton name="Action" @click="onManual" />
          </div>
          <InformationText
            text="버튼을 클릭하면 NOGI의 자동 실행 주기를 기다릴 필요 없이, 즉시 실행할 수 있어요."
          />
        </li>
      </ul>
    </li>

    <!--    notice-->
    <li class="mb-10">
      <h2 class="font-noto_sans_b text-lg">Notice</h2>
      <hr class="border-main my-2" />
      <ul class="border border-main p-4">
        <li class="flex items-center justify-between gap-3">
          <span class="text-sm">
            GitHub 전송 성공 여부를 Issue로 알림 받기
          </span>
          <OnOffToggleButton :isOn="true" />
        </li>
      </ul>
    </li>

    <!--    Danger Zone-->
    <li class="mb-10">
      <h2 class="font-noto_sans_b text-lg">Danger Zone</h2>
      <hr class="border-main my-4" />
      <ul class="border border-danger rounded-sm p-4">
        <li class="flex justify-between items-center">
          <span class="text-sm">회원탈퇴</span>
          <DangerButton name="Delete" />
        </li>
      </ul>
    </li>
  </ul>
</template>
