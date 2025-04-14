<script setup>
import { onMounted } from 'vue';
import { NoticeManager } from '@/manager/notice/NoticeManager.js';
import { useRoute } from 'vue-router';
import SPagination from '@/shared/common/SPagination.vue';
import CSettingTitle from '@/views/user/setting/components/CSettingTitle.vue';
import { AuthManager } from '@/manager/auth/AuthManager.js';
import { toYMD } from '../../utils/dateFormat.js';

const notice = new NoticeManager();
const route = useRoute();
const auth = new AuthManager();

onMounted(() => {
  notice.loadNotice(route.params.noticeId);
  if (auth.isAdmin()) {
    notice.loadNoticeRecipients(route.params.noticeId);
  }
});

const onChangePage = (pageNo) => {
  notice.loadNoticeRecipients(route.params.noticeId, pageNo);
};
</script>

<template>
  <div>
    <h1>{{ notice.getNotice().value?.title }}</h1>
    <h1>{{ notice.getNotice().value?.content }}</h1>
    <h1>{{ toYMD(notice.getNotice().value?.createdAt) }}</h1>

    <!--    todo: 컴포넌트로 빼기-->
    <div v-if="auth.isAdmin()">
      <CSettingTitle
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
                scope="col"
                class="w-8/12 sm:w-9/12 md:w-10/12 px-6 py-3 rounded-tl-md rounded-bl-md"
              >
                이름
              </th>
              <th scope="col" class="w-4/12 sm:w-3/12 md:w-2/12 px-6 py-3">
                결과
              </th>
              <th
                scope="col"
                class="w-4/12 sm:w-3/12 md:w-2/12 px-6 py-3 rounded-tr-md rounded-br-md"
              >
                날짜
              </th>
            </tr>
          </thead>
          <tbody>
            <tr
              class="cursor-pointer hover:bg-main"
              v-for="(item, index) in notice.getNotices().value"
              :key="index"
            >
              <th
                scope="row"
                class="w-8/12 sm:w-9/12 md:w-10/12 px-6 py-4 rounded-tl-md rounded-bl-md"
              >
                {{ item?.title }}
              </th>
              <th scope="row" class="w-8/12 sm:w-9/12 md:w-10/12 px-6 py-4">
                {{ item?.title }}
              </th>
              <th
                scope="row"
                class="w-4/12 sm:w-3/12 md:w-2/12 px-6 py-4 rounded-tr-md rounded-br-md text-xs"
              >
                {{ toYMD(item?.createdAt) }}
              </th>
            </tr>
          </tbody>
        </table>
      </div>
      <SPagination
        class="mt-10"
        :total="notice.getRecipientsPagination().total"
        :page="notice.getRecipientsPagination().page"
        :size="notice.getRecipientsPagination().size"
        @changePage="onChangePage"
      />
    </div>
  </div>
</template>
