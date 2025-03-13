<script setup>
import { EditorContent, useEditor } from '@tiptap/vue-3';
import StarterKit from '@tiptap/starter-kit';
import Image from '@tiptap/extension-image';
import { onBeforeMount, onBeforeUnmount, watch } from 'vue';
import STextEditorMenu from '@/shared/editor/STextEditorMenu.vue';
import { Placeholder } from '@tiptap/extension-placeholder';
import { Table } from '@tiptap/extension-table';
import { TableRow } from '@tiptap/extension-table-row';
import { TableHeader } from '@tiptap/extension-table-header';
import { TableCell } from '@tiptap/extension-table-cell';
import { Underline } from '@tiptap/extension-underline';
import { Link } from '@tiptap/extension-link';

const props = defineProps({
  content: {
    type: String
  },
  hasActiveEditor: {
    type: Boolean,
    default: false
  },
  contentStyle: {
    type: String
  }
});
const emit = defineEmits(['registerPost', 'addImage', 'fileSizeValidation']);
const editor = useEditor({
  content: props.content,
  editable: props.hasActiveEditor,
  onUpdate: () => {
    // this.$emit('update:modelValue', this.editor.getHTML());
    // JSON일 경우
    // this.$emit("update:modelValue", this.editor.getJSON());
  },
  editorProps: {
    attributes: {
      class: props.contentStyle
    }
  },
  extensions: [
    Link,
    Image,
    TableRow,
    TableCell,
    Underline,
    StarterKit,
    TableHeader,
    Table.configure({
      resizable: true
    }),
    Placeholder.configure({
      placeholder: '선택한 카테고리에 맞는 당신의 이야기를 적어주세요.'
    })
  ]
});

onBeforeMount(async () => {});

onBeforeUnmount(() => {
  editor.value.destroy();
});

watch(
  () => props.content,
  (content) => {
    editor.value.commands.setContent(content);
  }
);

const registerPost = () => {
  emit('registerPost', {
    html: editor.value.getHTML(),
    text: editor.value.getText()
  });
};

const uploadImage = async (file) => {
  // 이미지 서버에 업로드 경우
  // const validation = fileManager.checkValidation(file);
  // if (validation.hasFail) {
  //   emit('fileSizeValidation', validation);
  //   return;
  // }
  //
  // const response = await fileManager.upload(file);
  // if (response) {
  //   editor.value.commands.setImage({ src: response.path });
  //   emit('addImage', response.externalId);
  // }

  // base64로 변환 경우
  const reader = new FileReader();
  reader.readAsDataURL(file);
  reader.onload = () => {
    const base64 = reader.result;
    emit('addImage', base64);
    editor.value.commands.setImage({ src: base64 });
  };
};
</script>

<template>
  <div>
    <!--    todo: 활성화 메뉴 props로 받기(key로 처리), 메뉴를 감싸고잇는 dev style props로 받기-->
    <STextEditorMenu
      v-if="hasActiveEditor"
      :editor="editor"
      @registerPost="registerPost"
      @uploadImage="uploadImage"
    />
    <editor-content :editor="editor" />
  </div>
</template>

<style>
/* 기본 텍스트 스타일 */
.tiptap * {
  @apply leading-[2em] tracking-wider sm:leading-[2em];
}

/* 제목 스타일 */
.tiptap h1 {
  @apply font-noto_sans_b py-3 text-2xl sm:text-3xl text-white;
}

.tiptap h2 {
  @apply font-noto_sans_b py-2 text-xl sm:text-2xl text-gray-200;
}

.tiptap h3 {
  @apply font-noto_sans_m py-1 text-lg sm:text-xl text-gray-300;
}

/* 일반 텍스트 */
.tiptap p {
  @apply text-sm sm:text-base leading-normal sm:leading-relaxed;
}

/* 강조 */
.tiptap strong {
  @apply font-noto_sans_b text-white;
}

/* 코드 블록 */
.tiptap code {
  @apply font-noto_sans_m text-[#ff8b8b] bg-[#2b2b2b] px-3 py-1 rounded-md;
}

/* 인용 블록 */
.tiptap blockquote {
  @apply border-l-2 border-gray-500 pl-3 ml-3 text-gray-300 bg-[#2a2a2a] p-2 rounded-md;
}

/* 목록 스타일 */
.tiptap ul li {
  @apply list-disc ml-10 text-gray-300;
}

.tiptap ol li {
  @apply list-decimal ml-10 text-gray-300;
}

/* 구분선 */
.tiptap hr {
  @apply my-3 border-gray-600;
}

/* 링크 스타일 */
.tiptap a {
  @apply text-blue-700 bg-[#2a2a2a] px-2 py-1 rounded-md no-underline transition-colors duration-200;
}

.tiptap a:hover {
  @apply bg-[#3a3a3a] text-blue-500;
}

/* 이미지 스타일 */
.tiptap img {
  @apply rounded-md my-3 w-full object-scale-down max-h-[500px] border border-gray-600;
}

/* 테이블 스타일 */
.tiptap table {
  @apply max-w-full border-collapse border border-gray-600;
}

.tiptap table th,
.tiptap table td {
  @apply border border-gray-600 p-2 text-left text-gray-200;
}

.tiptap table th {
  @apply bg-[#3a3a3a] text-white;
}

.tiptap table tr:nth-child(even) td {
  @apply bg-[#2b2b2b];
}

/* 테이블 선택 영역 */
.tiptap .selectedCell:after {
  z-index: 2;
  position: absolute;
  content: '';
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  background: rgba(150, 150, 255, 0.3);
  pointer-events: none;
}

/* 컬럼 리사이즈 핸들 */
.tiptap .column-resize-handle {
  position: absolute;
  right: -2px;
  top: 0;
  bottom: -2px;
  width: 4px;
  background-color: #66aaff;
  pointer-events: none;
}

/* 리사이즈 커서 */
.tiptap .resize-cursor {
  cursor: ew-resize;
  cursor: col-resize;
}

/* 에디터 빈 칸 placeholder */
.tiptap p.is-editor-empty:first-child::before {
  content: attr(data-placeholder);
  float: left;
  color: #757575;
  pointer-events: none;
  height: 0;
}
</style>
