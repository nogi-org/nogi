<script setup>
import STextEditorContent from '@/shared/editor/STextEditorContent.vue';
import { ref } from 'vue';

const base64Image = ref('');
const text = ref('');

const addImage = (base64) => {
  console.log('base64 : ', base64);
};

const handleFileUpload = (event) => {
  const file = event.target.files[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = (e) => {
      base64Image.value = e.target.result;
    };
    reader.readAsDataURL(file);
  }
};

const copyToClipboard = async (target) => {
  try {
    if (target === 'image') {
      await navigator.clipboard.writeText(base64Image.value);
    } else {
      await navigator.clipboard.writeText(text.value);
    }
  } catch (err) {
    alert('복사 실패: ' + err);
  }
};

const registerPost = (data) => {
  text.value = data.html;
  window.scrollTo({ top: document.body.scrollHeight, behavior: 'smooth' });
};
</script>

<template>
  <div>
    <div class="mb-10">
      <input
        id="file-upload"
        accept="image/*"
        class="hidden"
        type="file"
        @change="handleFileUpload"
      />
      <div class="flex justify-end items-center gap-2 mb-2">
        <label
          class="cursor-pointer bg-action px-3 py-2 rounded-md text-white text-xs"
          for="file-upload"
        >
          이미지 변환
        </label>
        <button @click="copyToClipboard('image')">
          <font-awesome-icon
            class="bg-main p-2 rounded-md"
            icon="fa-solid fa-copy"
          />
        </button>
      </div>
      <p
        class="border border-main rounded-md p-2 h-[200px] max-h-[200px] whitespace-pre-wrap break-all overflow-scroll scrollbar-hide"
      >
        {{ base64Image }}
      </p>
    </div>

    <STextEditorContent
      :content-style="'border rounded-md outline-0 min-h-[400px] p-3'"
      :has-active-editor="true"
      @addImage="addImage"
      @registerPost="registerPost"
    />
    <div class="mt-5 flex flex-col items-end">
      <button @click="copyToClipboard('text')">
        <font-awesome-icon
          class="bg-main p-2 rounded-md"
          icon="fa-solid fa-copy"
        />
      </button>
      <p
        class="w-full border border-main rounded-md p-2 h-[250px] max-h-[500px] whitespace-pre-wrap break-all overflow-scroll scrollbar-hide"
      >
        {{ text }}
      </p>
    </div>
  </div>
</template>

<style scoped>
.scrollbar-hide {
  -ms-overflow-style: none; /* IE, Edge */
  scrollbar-width: none; /* Firefox */
}

.scrollbar-hide::-webkit-scrollbar {
  display: none; /* Chrome, Safari */
}
</style>
