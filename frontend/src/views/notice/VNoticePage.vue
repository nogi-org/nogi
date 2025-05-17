<script setup>
import { onMounted } from 'vue';
import { NoticeManager } from '@/manager/notice/NoticeManager.js';
import { useRoute } from 'vue-router';
import SPagination from '@/shared/common/SPagination.vue';
import SMainTitle from '@/shared/title/SMainTitle.vue';
import { AuthManager } from '@/manager/auth/AuthManager.js';
import STextEditorContent from '@/shared/editor/STextEditorContent.vue';
import * as dateFormat from '@/utils/dateFormat.js';
import { toYMD } from '@/utils/dateFormat.js';

const notice = new NoticeManager();
const route = useRoute();
const auth = new AuthManager();

onMounted(() => {
  notice.loadNotice(route.params.noticeId);
  if (auth.isAdmin()) {
    notice.loadNoticeRecipients(route.params.noticeId);
  }
});

const onChangePage = pageNo => {
  notice.loadNoticeRecipients(route.params.noticeId, pageNo);
};
</script>

<template>
  <div>
    <div class="mb-8 border-b border-b-main">
      <h1 class="text-2xl">{{ notice.getNotice().value?.title }}</h1>
      <p class="text-neutral text-xs text-right mb-2">
        {{ dateFormat.toYMD(notice.getNotice().value?.createdAt) }}
      </p>
    </div>
    <STextEditorContent
      :content="notice.getNotice().value?.content"
      class="mb-20"
    />

    <!--    todo: 컴포넌트로 빼기-->
    <div v-if="auth.isAdmin()">
      <SMainTitle
        :title="`공지 발행(${notice.getRecipientsPagination().total})`"
      />
      <div class="flex justify-end items-center">
        <button>드롭다운메뉴</button>
        <button>재발행</button>
      </div>

      <div class="relative overflow-x-auto rounded-md">
        <table class="w-full text-sm text-left text-neutral">
          <thead class="text-xs bg-main font-noto_sans_m">
            <tr>
              <th
                class="w-8/12 px-6 py-3 rounded-tl-md rounded-bl-md"
                scope="col"
              >
                이름
              </th>
              <th class="w-4/12 px-6 py-3" scope="col">결과</th>
              <th
                class="w-4/12 px-6 py-3 rounded-tr-md rounded-br-md"
                scope="col"
              >
                날짜
              </th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="(item, index) in notice.getRecipients().value"
              :key="index"
              class="cursor-pointer hover:bg-main"
            >
              <th
                class="w-8/12 px-6 py-4 rounded-tl-md rounded-bl-md"
                scope="row"
              >
                {{ item?.githubOwner }}
              </th>
              <th
                :class="
                  item?.isSuccess === true ? 'text-action' : 'text-danger'
                "
                class="w-8/12 px-6 py-4"
                scope="row"
              >
                {{ item?.isSuccess ? '성공' : '실패' }}
              </th>
              <th
                class="w-4/12 px-6 py-4 rounded-tr-md rounded-br-md text-xs"
                scope="row"
              >
                {{ toYMD(item?.createdAt) }}
              </th>
            </tr>
          </tbody>
        </table>
      </div>
      <SPagination
        :page="notice.getRecipientsPagination().page"
        :size="notice.getRecipientsPagination().size"
        :total="notice.getRecipientsPagination().total"
        class="mt-10"
        @changePage="onChangePage"
      />
    </div>
  </div>
</template>
