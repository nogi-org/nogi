<script setup>
import { onMounted, ref } from 'vue';
import { NotionManager } from '@/manager/notion/NotionManager.js';
import SMainTitle from '@/shared/title/SMainTitle.vue';
import SSubTitle from '@/shared/title/SSubTitle.vue';
import SSimpleTextInput from '@/shared/input/SSimpleTextInput.vue';
import SActionButton from '@/shared/buttons/SActionButton.vue';

const notion = new NotionManager();
const richTextProperty = ref();

onMounted(() => {
  updateRichText();
});

const updateRichText = event => {
  richTextProperty.value = notion.setRichTextProperty(event?.target?.value);
};

const createRichTextProperty = () => {
  notion.createRichTextProperty(richTextProperty.value);
};
</script>

<template>
  <div>
    <SMainTitle title="Notion Databse Property Create" />
    <div class="flex justify-between items-center">
      <SSubTitle title="Rich Text Type" />
      <SActionButton name="등록" @action="createRichTextProperty" />
    </div>
    <SSimpleTextInput
      class="border-2 rounded-md outline-none mt-3"
      placeholder="입력된 값이 속성이름으로 저장됩니다."
      @input="updateRichText"
    />
    <div class="bg-main p-2 rounded-md mt-3">
      <pre
        class="text-xs break-words whitespace-pre-wrap"
        v-text="JSON.stringify(richTextProperty, null, 2)"
      />
    </div>
  </div>
</template>
