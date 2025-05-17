<script setup>
import STextEditorContent from '@/shared/editor/STextEditorContent.vue';
import { onBeforeMount, reactive, ref } from 'vue';
import { GuideManager } from '@/manager/guide/GuideManager.js';
import SFullSizeImageModal from '@/shared/modal/SFullSizeImageModal.vue';
import { useSpinnerStore } from '@/stores/spinnerStore.js';

const userGuideManager = new GuideManager();
const userGuides = ref([]);
const spinnerStore = useSpinnerStore();

onBeforeMount(async () => {
  await getUserGuides();
});

// todo: userGuideManager 에 넣기
const getUserGuides = async () => {
  spinnerStore.on();
  const response = await userGuideManager.getUserGuides();
  const step = [10, 11, 12, 13, 14, 15, 16, 17];
  userGuides.value = response.filter(guide => step.includes(guide.step));
  spinnerStore.off();
};

const confirmModalAction = reactive({
  message: '',
  hasActive: false
});

const showFullProfileImage = image => {
  confirmModalAction.hasActive = true;
  confirmModalAction.message = image;
};

const toggleFullImage = hasAction => {
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
          class="mb-3 rounded-md cursor-pointer border-main border"
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
