<script setup>
import { onMounted } from 'vue';
import { NoticeManager } from '@/manager/notice/NoticeManager.js';
import CSettingTitle from '@/views/user/setting/components/CSettingTitle.vue';
import SPagination from '@/shared/common/SPagination.vue';
import { toYMD } from '../../utils/dateFormat.js';

const notice = new NoticeManager();

onMounted(async () => {
  await notice.loadNotices();
});

const goToNoticeDetail = (id) => {
  notice.goToNotice(id);
};

const onChangePage = async (pageNo) => {
  await notice.loadNotices(pageNo);
};
</script>

<template>
  <div>
    <CSettingTitle :title="`공지사항 (${notice.getPagination().total})`" />
    <div class="relative overflow-x-auto rounded-md">
      <table class="w-full text-sm text-left text-neutral">
        <thead class="text-xs bg-main font-noto_sans_m">
          <tr>
            <th
              scope="col"
              class="w-8/12 sm:w-9/12 md:w-10/12 px-6 py-3 rounded-tl-md rounded-bl-md"
            >
              제목
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
            @click="goToNoticeDetail(item.id)"
          >
            <th
              scope="row"
              class="w-8/12 sm:w-9/12 md:w-10/12 px-6 py-4 rounded-tl-md rounded-bl-md"
            >
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
      :total="notice.getPagination().total"
      :page="notice.getPagination().page"
      :size="notice.getPagination().size"
      @changePage="onChangePage"
    />
  </div>
</template>
