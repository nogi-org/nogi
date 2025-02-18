<script setup>
import TextEditorContent from '@/components/editor/TextEditorContent.vue';
import { onBeforeMount, reactive, ref } from 'vue';
import { UserGuideManager } from '@/manager/user/UserGuideManager.js';
import FullSizeImageModal from '@/components/modal/FullSizeImageModal.vue';

const userGuideManager = new UserGuideManager();
const userGuides = ref([]);

onBeforeMount(async () => {
  await getUserGuides();
});

const getUserGuides = async () => {
  const response = await userGuideManager.getUserGuides();
  userGuides.value = response;
};

const confirmModalAction = reactive({
  message: '',
  hasActive: false
});

const showFullProfileImage = (image) => {
  confirmModalAction.hasActive = true;
  confirmModalAction.message = image;
};

const toggleFullImage = (hasAction) => {
  confirmModalAction.hasActive = hasAction;
};
</script>

<template>
  <div>
    <ul>
      <li
        v-for="guide in userGuides"
        :key="guide.id"
        class="mb-16 last:mb-0 rounded-md gap-5 flex flex-col md:flex-row md:justify-between md:items-center"
      >
        <img
          :src="guide.image"
          alt="가이드 이미지"
          class="rounded-md md:w-[60%] cursor-pointer border-main border"
          @click="showFullProfileImage(guide.image)"
        />
        <TextEditorContent
          class="md:w-[40%]"
          :content="guide.content"
          :content-style="'outline-0'"
        />
      </li>
    </ul>
    <FullSizeImageModal
      :action="confirmModalAction"
      @onAction="toggleFullImage"
    />
  </div>
</template>
