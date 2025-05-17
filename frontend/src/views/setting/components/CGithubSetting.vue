<script setup>
import SMainTitle from '@/shared/title/SMainTitle.vue';
import SConnectionStatus from '@/shared/common/SConnectionStatus.vue';
import SActionButton from '@/shared/buttons/SActionButton.vue';
import SSubTitle from '@/shared/title/SSubTitle.vue';
import SSimpleTextCallout from '@/shared/callout/SSimpleTextCallout.vue';
import { inject, onMounted, ref, watch } from 'vue';
import SSimpleTextInput from '@/shared/input/SSimpleTextInput.vue';
import SSpinnerInner from '@/shared/spinner/SSpinnerInner.vue';
import SWarningCallout from '@/shared/callout/SWarningCallout.vue';

const user = inject('userManager');
const github = inject('githubManager');

const githubFrom = ref({
  repositoryName: '',
  owner: '',
  email: ''
});

watch(user.me, info => {
  githubFrom.value.repositoryName = info.githubRepository;
  githubFrom.value.owner = info.githubOwner;
  githubFrom.value.email = info.githubEmail;
});

onMounted(() => {
  github.getConnectedGithub();
});

const bindRepositoryName = repository => {
  githubFrom.value.repositoryName = repository.name;
};

const checkConnectedGithub = () => {
  github.getConnectedGithub();
  user.getMe();
};

const saveRepositoryName = async () => {
  if (!github.isSaveValidationRepositoryName(githubFrom.value.repositoryName)) {
    return;
  }
  await user.updateMe({ githubRepository: githubFrom.value.repositoryName });
  await github.getConnectedGithub();
};

const saveOwner = async () => {
  if (!github.isSaveValidationOwner(githubFrom.value.owner)) {
    return;
  }
  await user.updateMe({ githubOwner: githubFrom.value.owner });
  await github.getConnectedGithub();
};

const saveEmail = async () => {
  if (!github.isSaveValidationEmail(githubFrom.value.email)) {
    return;
  }
  await user.updateMe({ githubEmail: githubFrom.value.email });
  await github.getConnectedGithub();
};
</script>

<template>
  <div>
    <SMainTitle title="Github" />
    <ul class="border border-main p-4">
      <!--      connected -->
      <li class="flex justify-between border-b border-main pb-4 items-center">
        <div>
          <SConnectionStatus
            :isConnected="github.connection.value?.isGithubValid"
          />
          <SWarningCallout
            v-if="github.connection.value?.isGithubValid === false"
            class="mt-1 text-danger"
            text="Github정보와 아래정보가 동일한지 확인해주세요."
          />
        </div>
        <SActionButton name="연결 확인" @action="checkConnectedGithub" />
      </li>

      <!--          repository -->
      <li class="border-b border-main py-4">
        <SSubTitle title="Repository name" />
        <div class="sm:flex sm:justify-between sm:items-center sm:mb-4">
          <SSimpleTextInput
            v-model="githubFrom.repositoryName"
            :class="{
              'border-2 border-danger rounded-md outline-none':
                github.connection.value.isGithubRepositoryValid === false
            }"
            class="sm:w-[50%]"
            placeholder="Repository Name"
          />
          <SActionButton
            class="sm:w-[50%] text-right mt-1 sm:mt-0"
            name="저장"
            @action="saveRepositoryName"
          />
        </div>

        <div class="mt-8 flex items-center gap-2">
          <SSubTitle title="Github Public Repository" />
          <span class="text-xs font-noto_sans_m">
            {{ github.connection.value?.githubRepositories?.length }}
          </span>
        </div>
        <SSimpleTextCallout
          class="mb-2"
          text="기존 Git Repository를 NOGI와 연결할 수 있어요"
        />
        <ul
          v-if="github.connection.value.githubRepositories"
          class="max-h-40 overflow-y-scroll"
        >
          <li
            v-for="(item, index) in github.connection.value?.githubRepositories"
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
        <div v-else class="h-40">
          <SSpinnerInner />
        </div>
      </li>

      <!--          owner -->
      <li class="border-b border-main py-4">
        <SSubTitle title="Owner" />
        <div class="sm:flex sm:justify-between sm:items-center">
          <SSimpleTextInput
            v-model="githubFrom.owner"
            :class="{
              'border-2 border-danger rounded-md outline-none':
                github.connection.value.isGithubOwnerValid === false
            }"
            class="sm:w-[50%]"
            placeholder="NOGI Owner"
          />
          <SActionButton
            class="sm:w-[50%] text-right mt-1 sm:mt-0"
            name="저장"
            @action="saveOwner"
          />
        </div>
      </li>

      <!--          email -->
      <li class="py-4">
        <SSubTitle title="Email" />
        <div class="sm:flex sm:justify-between sm:items-center">
          <SSimpleTextInput
            v-model="githubFrom.email"
            :class="{
              'border-2 border-danger rounded-md outline-none':
                github.connection.value.isGithubEmailValid === false
            }"
            class="sm:w-[50%]"
            placeholder="NOGI Email"
          />
          <SActionButton
            class="sm:w-[50%] text-right mt-1 sm:mt-0"
            name="저장"
            @action="saveEmail"
          />
        </div>
      </li>
    </ul>
  </div>
</template>
