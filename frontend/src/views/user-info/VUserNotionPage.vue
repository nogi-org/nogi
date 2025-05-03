<script setup>
import { onMounted, ref } from 'vue';
import { NotionManager } from '@/manager/notion/NotionManager.js';
import { useRoute } from 'vue-router';
import { toKRFromUTC } from '@/utils/dateFormat.js';

const route = useRoute();
const notion = new NotionManager();
const userId = route.params.userId;
const pageId = route.params.pageId;
const expandedItems = ref({});

const toggleItem = itemId => {
  expandedItems.value[itemId] = !expandedItems.value[itemId];
};

const copy = item => {
  const itemText = JSON.stringify(item, null, 2);
  navigator.clipboard
    .writeText(itemText)
    .then(() => {
      console.log('복사 성공!');
    })
    .catch(err => {
      console.error('복사 실패:', err);
    });
};

onMounted(() => {
  notion.getDetailPage({ userId: userId, pageId: pageId });
});
</script>

<template>
  <div>
    <ul>
      <li
        v-for="item in notion.page.value?.results"
        :key="item.id"
        class="mb-5 border-b pb-2 border-main"
      >
        <div class="flex gap-3 items-center">
          <span class="px-2 py-1 bg-action rounded-md text-xs">
            Type: {{ item.type }}
          </span>
          <span
            :class="item.has_children ? 'bg-action' : 'bg-main'"
            class="px-2 py-1 rounded-md text-xs"
          >
            HasChildren: {{ item.has_children }}
          </span>
        </div>
        <div class="my-3">
          <p
            v-for="item1 in item[item.type]?.['rich_text']"
            :key="item1.plain_text"
          >
            {{ item1.plain_text }}
          </p>
        </div>
        <div class="flex gap-3 items-center justify-between">
          <span class="text-neutral text-xs">{{ item.id }}</span>
          <p class="text-right text-xs text-neutral">
            {{ toKRFromUTC(item.created_time) }}
          </p>
        </div>
        <div class="mt-2">
          <button
            class="text-xs px-2 py-1 bg-main rounded-md flex items-center gap-2"
            @click="toggleItem(item.id)"
          >
            <span>{{ expandedItems[item.id] ? '접어두기' : '상세보기' }}</span>
            <font-awesome-icon
              v-if="expandedItems[item.id]"
              :icon="['fas', 'caret-up']"
            />
            <font-awesome-icon v-else :icon="['fas', 'caret-down']" />
          </button>
          <div
            v-if="expandedItems[item.id]"
            class="mt-2 bg-main p-2 rounded-md"
          >
            <div class="text-right mr-3">
              <font-awesome-icon
                :icon="['fas', 'copy']"
                class="cursor-pointer"
                @click="copy(item)"
              />
            </div>
            <pre
              class="text-xs break-words whitespace-pre-wrap"
              v-text="JSON.stringify(item, null, 2)"
            />
          </div>
        </div>
      </li>
    </ul>
  </div>
</template>

<style scoped></style>
