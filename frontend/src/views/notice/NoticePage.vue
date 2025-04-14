<script setup>
import { onMounted } from 'vue';
import { NoticeManager } from '@/manager/notice/NoticeManager.js';
import { useRoute } from 'vue-router';
import { toYMD } from '@/utils/dateFormat.js';

const notice = new NoticeManager();
// const router = useRouter();
const route = useRoute();

onMounted(async () => {
  await notice.loadNotice(route.params.noticeId);
});
</script>

<template>
  <div>
    <h1>{{ notice.getNotice().value?.title }}</h1>
    <h1>{{ notice.getNotice().value?.content }}</h1>
    <h1>{{ toYMD(notice.getNotice().value?.createdAt) }}</h1>
  </div>
</template>
