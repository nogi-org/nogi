<script setup>
import { onMounted } from 'vue';
import { NotionManager } from '@/manager/notion/NotionManager.js';
import { useRoute } from 'vue-router';
import SMainTitle from '@/shared/title/SMainTitle.vue';

const route = useRoute();
const notion = new NotionManager();
const userId = route.params.userId;

onMounted(() => {
  notion.getNotionDatabase(userId);
});
</script>

<template>
  <div>
    <SMainTitle title="Notion Databse Property" />
    <div class="bg-main p-2 rounded-md">
      <pre
        class="text-xs break-words whitespace-pre-wrap"
        v-text="JSON.stringify(notion.database.value, null, 2)"
      />
    </div>
  </div>
</template>
