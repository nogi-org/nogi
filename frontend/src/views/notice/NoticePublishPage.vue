<script setup>
import STextEditorContent from '@/shared/editor/STextEditorContent.vue';
import { NoticeManager } from '@/manager/notice/NoticeManager.js';

const notice = new NoticeManager();

const registerPost = (data) => {
  notice.setNewNoticeContent(data);
  if (!notice.isValidationPublish()) {
    return;
  }
  notice.publish();
};
</script>

<template>
  <div>
    <input
      type="text"
      class="bg-main w-full rounded-sm"
      placeholder="제목입력"
      v-model="notice.getNewNotice().value.title"
    />
    <STextEditorContent
      :content-style="'border rounded-md outline-0 min-h-[400px] p-3'"
      :has-active-editor="true"
      @registerPost="registerPost"
    />
  </div>
</template>
