<script setup>
import { onMounted, ref } from 'vue';
import EditorJS from '@editorjs/editorjs';

const editorHolder = ref(null);
let editor = null;

onMounted(() => {
  editor = new EditorJS({
    holder: editorHolder.value,
    tools: {}, // 필요한 툴 추가 가능 (예: Header, List 등)
    autofocus: true,
    placeholder: '내용을 입력하세요...'
  });
});

// onBeforeUnmount(() => {
//   if (editorInstance && typeof editorInstance.destroy === 'function') {
//     editorInstance.destroy();
//   }
// });
</script>

<template>
  <div>
    <h1>발행페이지</h1>
    <div ref="editorHolder" />
  </div>
</template>
