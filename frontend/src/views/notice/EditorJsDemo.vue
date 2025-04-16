<script setup>
import { onMounted, ref } from 'vue';
import EditorJS from '@editorjs/editorjs';
import Header from '@editorjs/header';
import EditorjsList from '@editorjs/list';
import Quote from '@cychann/editorjs-quote';
import Warning from '@editorjs/warning';
import Paragraph from '@editorjs/paragraph';
import Delimiter from '@editorjs/delimiter';
import Alert from 'editorjs-alert';
import ToggleBlock from 'editorjs-toggle-block';
import LinkTool from '@editorjs/link';
import Title from 'title-editorjs';

const editorHolder = ref(null);
let editor = null;

// https://github.com/editor-js/awesome-editorjs  (에디터 툴)
onMounted(() => {
  editor = new EditorJS({
    holder: editorHolder.value,
    autofocus: true,
    // readOnly: true,
    placeholder: '명령어 사용 시에는 "/"를 누르세요.',
    tools: {
      paragraph: {
        class: Paragraph,
        inlineToolbar: true
      },
      header: {
        class: Header,
        inlineToolbar: true,
        config: {
          placeholder: '제목1을 입력해주세요.',
          levels: [1, 2, 3],
          defaultLevel: 1,
          defaultAlignment: 'left'
        }
      },
      quote: {
        class: Quote,
        inlineToolbar: true,
        config: {
          quotePlaceholder: 'Enter a quote',
          captionPlaceholder: 'Quote Caption',
          defaultType: 'verticalLine'
        }
      },
      warning: {
        class: Warning,
        inlineToolbar: true,
        config: {
          titlePlaceholder: '제목',
          messagePlaceholder: '내용'
        }
      },
      list: {
        class: EditorjsList,
        inlineToolbar: true
      },
      delimiter: Delimiter,
      alert: Alert,
      toggle: {
        class: ToggleBlock,
        inlineToolbar: true
      },
      linkTool: {
        class: LinkTool
      },
      title: Title
    }
  });
});

// onBeforeUnmount(() => {
//   if (editorInstance && typeof editorInstance.destroy === 'function') {
//     editorInstance.destroy();
//   }
// });

const saveData = async () => {
  const response = await editor.save();
  console.log('saveData : ', response);
};
</script>

<template>
  <div>
    <h1>발행페이지</h1>
    <button @click="saveData">저장</button>
    <div ref="editorHolder" class="border-danger border" />
  </div>
</template>
