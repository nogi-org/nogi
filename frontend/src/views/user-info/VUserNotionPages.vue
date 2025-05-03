<script setup>
import { NotionManager } from '@/manager/notion/NotionManager.js';
import { useRoute } from 'vue-router';
import { toYMDHMS } from '../../utils/dateFormat.js';
import SActionButton from '@/shared/buttons/SActionButton.vue';
import router from '@/router/router.js';
import SMainTitle from '@/shared/title/SMainTitle.vue';
import SBoxCallout from '@/shared/callout/SBoxCallout.vue';

const route = useRoute();
const notion = new NotionManager();
const userId = route.params.userId;

const goToPage = isNext => {
  notion.getPagesOfUser({
    userId: userId,
    nextCursor: notion.pages.value.nextCursor,
    isNext: isNext
  });
};

const goToDetailPage = page => {
  router.push({
    name: 'userNotionPage',
    params: { userId: userId, pageId: page.id }
  });
};

const getNotionPages = () => {
  notion.getPagesOfUser({ userId: userId });
};
</script>

<template>
  <div>
    <SMainTitle title="Notion Pages" />
    <SActionButton class="text-right" name="조회" @action="getNotionPages" />

    <SBoxCallout
      v-if="notion.pages.value == null"
      class="mt-5"
      text="해당 유저의 Notion 페이지를 확인하려면 ‘조회’ 버튼을 눌러주세요."
    />
    <div v-else>
      <ul class="mt-5">
        <li
          v-for="(item, index) in notion.pages.value?.pages"
          :key="index"
          class="pb-5 border-b border-main mb-5 last:mb-0 cursor-pointer"
          @click="goToDetailPage(item)"
        >
          <ul class="flex gap-3">
            <li class="bg-action text-xs px-2 py-0.5 rounded-md">
              {{ item.status }}
            </li>
            <li
              v-for="item1 in item.categories"
              class="bg-neutral text-xs px-2 py-0.5 rounded-md"
            >
              {{ item1 }}
            </li>
          </ul>

          <div class="my-2">
            <p class="text-2xl">{{ item.title }}</p>
            <p class="text-neutral text-sm mt-1">{{ item.commitMessage }}</p>
            <p class="text-neutral text-sm flex items-center justify-end gap-1">
              <font-awesome-icon :icon="['fab', 'github']" class="text-base" />
              {{ toYMDHMS(item.commitDate) }}
            </p>
          </div>

          <div class="flex justify-between text-neutral text-xs">
            <div>
              <p>PageId: {{ item.id }}</p>
              <p>DatabaseId: {{ item.databaseId }}</p>
            </div>
            <div class="text-right">
              <p>Create: {{ item.createdTime }}</p>
              <p>Last Edit: {{ item.lastEditedTime }}</p>
            </div>
          </div>
        </li>
      </ul>
      <div class="flex justify-center gap-2 mt-5">
        <SActionButton
          v-if="notion.prevCursors.value?.length > 0"
          name="이전"
          @action="goToPage(false)"
        />
        <SActionButton
          v-if="notion.pages.value?.isMore"
          name="다음"
          @action="goToPage(true)"
        />
      </div>
    </div>
  </div>
</template>

<style scoped></style>
