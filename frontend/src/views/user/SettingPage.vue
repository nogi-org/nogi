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
  user.getConnectedGithubInfo();
});

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

const deleteUser = () => {
  user.deleteUser();
};
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
          <ConnectionStatus
            :isConnected="user.githubInfo.value?.isGithubValid"
          />
          <ActionButton name="연결 확인" @action="createNewNotionDatabase" />
        </li>
        <li class="border-b border-main py-4">
          <h3
            class="font-noto_sans_m mb-2"
            :class="{
              'text-danger': !user.githubInfo.value?.isGithubRepositoryValid
            }"
          >
            Repository name
          </h3>
          <div class="flex justify-between items-center mb-4">
            <SimpleTextInput
              v-model="user.info.value.githubRepository"
              placeholder="Repository Name"
              class="w-[50%]"
            />
            <ActionButton
              name="저장"
              @action="createNewNotionDatabase"
              class="w-[50%] text-right"
            />
          </div>

          <div class="mt-8 flex items-center gap-2">
            <h3 class="font-noto_sans_m">Github Public Repository</h3>
            <span class="text-xs font-noto_sans_m">
              {{ user.githubInfo.value?.githubRepositories?.length }}
            </span>
          </div>
          <InformationText
            text="기존 Git Public Repository를 선택하여 NOGI와 연결할 수 있어요"
            class="mb-2"
          />
          <ul class="max-h-32 overflow-scroll">
            <li
              class="sm:flex sm:gap-5 mb-3"
              v-for="(item, index) in user.githubInfo.value?.githubRepositories"
              :key="index"
            >
              <p
                class="cursor-pointer text-neutral"
                @click="repositoryName = 'DEMO3'"
              >
                {{ item.name }}
              </p>
              <a
                :href="item.htmlUrl"
                target="_blank"
                rel="noopener noreferrer"
                class="text-action hover:underline break-all"
              >
                <font-awesome-icon icon="fa-solid fa-link" class="text-sm" />
                {{ item.htmlUrl }}
              </a>
            </li>
          </ul>
        </li>
        <li class="border-b border-main py-4">
          <h3
            class="font-noto_sans_m mb-2"
            :class="{
              'text-danger': !user.githubInfo.value?.isGithubOwnerValid
            }"
          >
            Owner
          </h3>
          <SimpleTextInput
            v-model="user.info.value.githubOwner"
            placeholder="Git Owner"
            class="w-[50%] mb-1"
            :disabled="true"
          />
          <div class="flex justify-between items-center">
            <SimpleTextInput
              v-model="user.info.value.githubOwner"
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
          <h3
            class="font-noto_sans_m mb-2"
            :class="{
              'text-danger': !user.githubInfo.value?.isGithubEmailValid
            }"
          >
            Email
          </h3>
          <SimpleTextInput
            v-model="repositoryName"
            placeholder="Git Email"
            class="w-[50%] mb-1"
            :disabled="true"
          />
          <div class="flex justify-between items-center">
            <SimpleTextInput
              v-model="user.info.value.githubEmail"
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
          <OnOffToggleButton
            :isOn="user.info.value.isNotificationAllowed"
            @action="user.updateNotificationAllow()"
          />
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
          <DangerButton name="Delete" @action="deleteUser" />
        </li>
      </ul>
    </li>
  </ul>
</template>
