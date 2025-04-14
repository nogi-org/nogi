<script setup>
import { onMounted, ref } from 'vue';
import EditorJS from '@editorjs/editorjs';
import Header from '@editorjs/header';
import EditorjsList from '@editorjs/list';
import Quote from '@editorjs/quote';
import Warning from '@editorjs/warning';

const editorHolder = ref(null);
let editor = null;

// https://github.com/editor-js/awesome-editorjs  (에디터 툴)
onMounted(() => {
  editor = new EditorJS({
    holder: editorHolder.value,
    autofocus: true,
    // readOnly: true,
    placeholder:
      '내용을 입력하세요... /를 입력하면 원하는 도구를 사용할 수 있어요.',
    tools: {
      header: {
        class: Header,
        inlineToolbar: true,
        config: {
          placeholder: '제목을 입력해주세요.',
          levels: [1, 2, 3],
          defaultLevel: 2
        }
      },
      list: {
        class: EditorjsList,
        inlineToolbar: true
      },
      quote: {
        class: Quote,
        inlineToolbar: true,
        config: {
          quotePlaceholder: 'Enter a quote',
          captionPlaceholder: "Quote's author"
        }
      },
      warning: {
        class: Warning,
        inlineToolbar: true,
        config: {
          titlePlaceholder: '제목',
          messagePlaceholder: '내용'
        }
      }
    }
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
    <div ref="editorHolder" class="border-danger border" />
  </div>
</template>
