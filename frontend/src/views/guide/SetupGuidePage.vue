<script setup>
import STextEditorContent from '@/shared/editor/STextEditorContent.vue';
import { onBeforeMount, reactive, ref } from 'vue';
import { UserGuideManager } from '@/manager/guide/UserGuideManager.js';
import SFullSizeImageModal from '@/shared/modal/SFullSizeImageModal.vue';
import { useSpinnerStore } from '@/stores/spinnerStore.js';

const userGuideManager = new UserGuideManager();
const userGuides = ref([]);
const spinnerStore = useSpinnerStore();

onBeforeMount(async () => {
  await getUserGuides();
});

// todo: userGuideManager 에 넣기
const getUserGuides = async () => {
  spinnerStore.on();
  const response = await userGuideManager.getUserGuides();
  userGuides.value = response;
  spinnerStore.off();
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
        <STextEditorContent
          :content="guide.content"
          :content-style="'outline-0'"
          class="md:w-[40%]"
        />
      </li>
    </ul>
    <SFullSizeImageModal
      :action="confirmModalAction"
      @onAction="toggleFullImage"
    />
  </div>
</template>
