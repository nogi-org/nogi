<script setup>
import { onMounted } from 'vue';
import { NoticeManager } from '@/manager/notice/NoticeManager.js';
import CSettingTitle from '@/views/setting/components/CSettingTitle.vue';
import SPagination from '@/shared/common/SPagination.vue';
import { toYMD } from '../../utils/dateFormat.js';
import SActionButton from '@/shared/buttons/SActionButton.vue';
import { AuthManager } from '@/manager/auth/AuthManager.js';

const notice = new NoticeManager();
const auth = new AuthManager();

onMounted(async () => {
  await notice.loadNotices();
});

const goToNoticeDetail = id => {
  notice.goToNotice(id);
};

const onChangePage = async pageNo => {
  await notice.loadNotices(pageNo);
};
</script>

<template>
  <div>
    <CSettingTitle :title="`공지사항 (${notice.getPagination().total})`" />
    <SActionButton
      v-if="auth.isAdmin()"
      class="flex justify-end py-3"
      name="신규발행"
      @action="notice.goToPublishPage()"
    />
    <div class="relative overflow-x-auto rounded-md">
      <table class="w-full text-sm text-left text-neutral">
        <thead class="text-xs bg-main font-noto_sans_m">
          <tr>
            <th
              class="w-8/12 sm:w-9/12 md:w-10/12 px-6 py-3 rounded-tl-md rounded-bl-md"
              scope="col"
            >
              제목
            </th>
            <th
              class="w-4/12 sm:w-3/12 md:w-2/12 px-6 py-3 rounded-tr-md rounded-br-md"
              scope="col"
            >
              날짜
            </th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="(item, index) in notice.getNotices().value"
            :key="index"
            class="cursor-pointer hover:bg-main"
            @click="goToNoticeDetail(item.id)"
          >
            <th
              class="w-8/12 sm:w-9/12 md:w-10/12 px-6 py-4 rounded-tl-md rounded-bl-md"
              scope="row"
            >
              {{ item?.title }}
            </th>
            <th
              class="w-4/12 sm:w-3/12 md:w-2/12 px-6 py-4 rounded-tr-md rounded-br-md text-xs"
              scope="row"
            >
              {{ toYMD(item?.createdAt) }}
            </th>
          </tr>
        </tbody>
      </table>
    </div>
    <SPagination
      :page="notice.getPagination().page"
      :size="notice.getPagination().size"
      :total="notice.getPagination().total"
      class="mt-10"
      @changePage="onChangePage"
    />
  </div>
</template>
