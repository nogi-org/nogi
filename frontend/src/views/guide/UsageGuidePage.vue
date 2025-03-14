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
  userGuides.value = response.filter((guide) => guide.step === 1);
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
      <li v-for="guide in userGuides" :key="guide.id" class="mb-16 rounded-md">
        <img
          :src="guide.image"
          alt="가이드 이미지"
          class="mb-8 rounded-md cursor-pointer border-main border"
          @click="showFullProfileImage(guide.image)"
        />
        <STextEditorContent
          :content="guide.content"
          content-style="outline-0"
        />
      </li>
    </ul>
    <SFullSizeImageModal
      :action="confirmModalAction"
      @onAction="toggleFullImage"
    />
  </div>
</template>
