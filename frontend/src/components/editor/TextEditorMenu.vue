<script setup>
defineProps({
  editor: {
    type: Object
  }
});

const emit = defineEmits(['uploadImage', 'registerPost']);

const uploadImage = (event) => {
  emit('uploadImage', event.target.files[0]);
};
</script>

<template>
  <div class="pt-2">
    <div class="text-right py-3 flex justify-end">
      <button
        @click="$emit('registerPost')"
        class="bg-action px-3 py-2 rounded-md text-white text-xs"
      >
        <font-awesome-icon icon="fa-solid fa-circle-check" class="mr-2" />
        <span>변환</span>
      </button>
    </div>
    <ul class="flex items-center gap-2 flex-wrap sm:gap-4 mb-4">
      <li
        class="text-xs"
        :class="{
          'bg-gray-300 rounded-md':
            editor && editor.isActive('heading', { level: 1 })
        }"
        @click="editor.chain().focus().toggleHeading({ level: 1 }).run()"
      >
        <button class="p-2 rounded-md border border-gray-300">
          <span class="font-noto_sans_b"
            ><font-awesome-icon icon="fa-solid fa-heading" />1
          </span>
        </button>
      </li>
      <li
        class="text-xs"
        :class="{
          'bg-gray-300 rounded-md':
            editor && editor.isActive('heading', { level: 2 })
        }"
        @click="editor.chain().focus().toggleHeading({ level: 2 }).run()"
      >
        <button class="p-2 rounded-md border border-gray-300">
          <span class="font-noto_sans_b"
            ><font-awesome-icon icon="fa-solid fa-heading" />2</span
          >
        </button>
      </li>
      <li
        class="text-xs"
        :class="{
          'bg-gray-300 rounded-md':
            editor && editor.isActive('heading', { level: 3 })
        }"
        @click="editor.chain().focus().toggleHeading({ level: 3 }).run()"
      >
        <button class="p-2 rounded-md border border-gray-300">
          <span class="font-noto_sans_b"
            ><font-awesome-icon icon="fa-solid fa-heading" />3</span
          >
        </button>
      </li>
      <li
        class="text-xs"
        :class="{ 'bg-gray-300 rounded-md': editor && editor.isActive('bold') }"
        @click="editor.chain().focus().toggleBold().run()"
      >
        <button class="p-2 rounded-md border border-gray-300">
          <font-awesome-icon icon="fa-solid fa-bold" />
        </button>
      </li>
      <li
        class="text-xs"
        @click="editor.chain().focus().toggleCode().run()"
        :class="{ 'bg-gray-300 rounded-md': editor && editor.isActive('code') }"
      >
        <button class="p-2 rounded-md border border-gray-300">
          <font-awesome-icon icon="fa-solid fa-code" />
        </button>
      </li>
      <li
        class="text-xs"
        @click="editor.chain().focus().toggleItalic().run()"
        :class="{
          'bg-gray-300 rounded-md': editor && editor.isActive('italic')
        }"
      >
        <button class="p-2 rounded-md border border-gray-300">
          <font-awesome-icon icon="fa-solid fa-italic" />
        </button>
      </li>
      <li
        class="text-xs"
        @click="editor.chain().focus().toggleUnderline().run()"
        :class="{
          'bg-gray-300 rounded-md': editor && editor.isActive('underline')
        }"
      >
        <button class="p-2 rounded-md border border-gray-300">
          <font-awesome-icon icon="fa-solid fa-underline" />
        </button>
      </li>
      <li
        class="text-xs"
        @click="editor.chain().focus().toggleLink().run()"
        :class="{ 'bg-gray-300 rounded-md': editor && editor.isActive('link') }"
      >
        <button class="p-2 rounded-md border border-gray-300">
          <font-awesome-icon icon="fa-solid fa-link" />
        </button>
      </li>
      <li
        class="text-xs"
        @click="editor.chain().focus().toggleStrike().run()"
        :class="{
          'bg-gray-300 rounded-md': editor && editor.isActive('strike')
        }"
      >
        <button class="p-2 rounded-md border border-gray-300">
          <font-awesome-icon icon="fa-solid fa-strikethrough" />
        </button>
      </li>
      <li
        class="text-xs"
        @click="editor.chain().focus().toggleBlockquote().run()"
        :class="{
          'bg-gray-300 rounded-md': editor && editor.isActive('blockquote')
        }"
      >
        <button class="p-2 rounded-md border border-gray-300">
          <font-awesome-icon icon="fa-solid fa-quote-left" />
        </button>
      </li>
      <li
        class="text-xs"
        @click="editor.chain().focus().toggleBulletList().run()"
        :class="{
          'bg-gray-300 rounded-md': editor && editor.isActive('bulletList')
        }"
      >
        <button class="p-2 rounded-md border border-gray-300">
          <font-awesome-icon icon="fa-solid fa-list-ul" />
        </button>
      </li>
      <li
        class="text-xs"
        @click="editor.chain().focus().toggleOrderedList().run()"
        :class="{
          'bg-gray-300 rounded-md': editor && editor.isActive('orderedList')
        }"
      >
        <button class="p-2 rounded-md border border-gray-300">
          <font-awesome-icon icon="fa-solid fa-list-ol" />
        </button>
      </li>
      <li
        class="text-xs"
        @click="editor.chain().focus().setHorizontalRule().run()"
      >
        <button class="p-2 rounded-md border border-gray-300">
          <font-awesome-icon icon="fa-solid fa-bars" />
        </button>
      </li>
      <!--      <li class="text-xs">-->
      <!--        <label-->
      <!--          for="file"-->
      <!--          class="cursor-pointer px-2 py-2.5 rounded-md border border-gray-300"-->
      <!--        >-->
      <!--          <font-awesome-icon icon="fa-regular fa-image" />-->
      <!--          <input-->
      <!--            type="file"-->
      <!--            id="file"-->
      <!--            accept="image/png, image/jpeg, image/jpg, image/gif"-->
      <!--            class="hidden"-->
      <!--            @change="uploadImage"-->
      <!--          />-->
      <!--        </label>-->
      <!--      </li>-->
      <li class="text-xs">
        <button
          @click="editor.commands.undo()"
          class="p-2 rounded-md border border-gray-300"
        >
          <font-awesome-icon icon="fa-solid fa-arrow-left-long" />
        </button>
      </li>
      <li class="text-xs">
        <button
          class="p-2 rounded-md border border-gray-300"
          @click="editor.commands.redo()"
        >
          <font-awesome-icon icon="fa-solid fa-arrow-right-long" />
        </button>
      </li>

      <!--      테이블 테스트-->
      <!--      <li class="text-xs">-->
      <!--        <button-->
      <!--          class="p-2 rounded-md border border-gray-300"-->
      <!--          @click="-->
      <!--            editor.commands.insertTable({-->
      <!--              rows: 3,-->
      <!--              cols: 3,-->
      <!--              withHeaderRow: true-->
      <!--            })-->
      <!--          "-->
      <!--        >-->
      <!--          <font-awesome-icon icon="fa-solid fa-table" class="mr-1" />CREATE-->
      <!--        </button>-->
      <!--      </li>-->
      <!--      <li class="text-xs">-->
      <!--        <button-->
      <!--          class="p-2 rounded-md border border-gray-300"-->
      <!--          @click="editor.commands.deleteTable()"-->
      <!--        >-->
      <!--          <font-awesome-icon icon="fa-solid fa-table" class="mr-1" />DELETE-->
      <!--        </button>-->
      <!--      </li>-->
      <!--      <li class="text-xs">-->
      <!--        <button-->
      <!--          @click="editor.commands.addColumnBefore()"-->
      <!--          class="p-2 rounded-md border border-gray-300"-->
      <!--        >-->
      <!--          <font-awesome-icon-->
      <!--            icon="fa-solid fa-table"-->
      <!--            class="mr-1"-->
      <!--          />AddColumnBefore-->
      <!--        </button>-->
      <!--      </li>-->
      <!--      <li class="text-xs">-->
      <!--        <button-->
      <!--          class="p-2 rounded-md border border-gray-300"-->
      <!--          @click="editor.commands.addColumnAfter()"-->
      <!--        >-->
      <!--          <font-awesome-icon-->
      <!--            icon="fa-solid fa-table"-->
      <!--            class="mr-1"-->
      <!--          />AddColumnAfter-->
      <!--        </button>-->
      <!--      </li>-->
      <!--      <li class="text-xs">-->
      <!--        <button-->
      <!--          class="p-2 rounded-md border border-gray-300"-->
      <!--          @click="editor.commands.deleteColumn()"-->
      <!--        >-->
      <!--          <font-awesome-icon-->
      <!--            icon="fa-solid fa-table"-->
      <!--            class="mr-1"-->
      <!--          />DeleteColumn-->
      <!--        </button>-->
      <!--      </li>-->
      <!--      <li class="text-xs">-->
      <!--        <button-->
      <!--          class="p-2 rounded-md border border-gray-300"-->
      <!--          @click="editor.commands.addRowBefore()"-->
      <!--        >-->
      <!--          <font-awesome-icon-->
      <!--            icon="fa-solid fa-table"-->
      <!--            class="mr-1"-->
      <!--          />AddRowBefore-->
      <!--        </button>-->
      <!--      </li>-->
      <!--      <li class="text-xs">-->
      <!--        <button-->
      <!--          class="p-2 rounded-md border border-gray-300"-->
      <!--          @click="editor.commands.addRowAfter()"-->
      <!--        >-->
      <!--          <font-awesome-icon icon="fa-solid fa-table" class="mr-1" />AddRowAfter-->
      <!--        </button>-->
      <!--      </li>-->
      <!--      <li class="text-xs">-->
      <!--        <button-->
      <!--          class="p-2 rounded-md border border-gray-300"-->
      <!--          @click="editor.commands.deleteRow()"-->
      <!--        >-->
      <!--          <font-awesome-icon icon="fa-solid fa-table" class="mr-1" />DeleteRow-->
      <!--        </button>-->
      <!--      </li>-->
      <!--      <li class="text-xs">-->
      <!--        <button-->
      <!--          class="p-2 rounded-md border border-gray-300"-->
      <!--          @click="editor.commands.mergeCells()"-->
      <!--        >-->
      <!--          <font-awesome-icon icon="fa-solid fa-table" class="mr-1" />MergeCells-->
      <!--        </button>-->
      <!--      </li>-->
      <!--      <li class="text-xs">-->
      <!--        <button-->
      <!--          class="p-2 rounded-md border border-gray-300"-->
      <!--          @click="editor.commands.splitCell()"-->
      <!--        >-->
      <!--          <font-awesome-icon icon="fa-solid fa-table" class="mr-1" />SplitCell-->
      <!--        </button>-->
      <!--      </li>-->
      <!--      <li class="text-xs">-->
      <!--        <button-->
      <!--          class="p-2 rounded-md border border-gray-300"-->
      <!--          @click="editor.commands.toggleHeaderColumn()"-->
      <!--        >-->
      <!--          <font-awesome-icon-->
      <!--            icon="fa-solid fa-table"-->
      <!--            class="mr-1"-->
      <!--          />HeaderColumn-->
      <!--        </button>-->
      <!--      </li>-->
      <!--      <li class="text-xs">-->
      <!--        <button-->
      <!--          class="p-2 rounded-md border border-gray-300"-->
      <!--          @click="editor.commands.toggleHeaderRow()"-->
      <!--        >-->
      <!--          <font-awesome-icon icon="fa-solid fa-table" class="mr-1" />HeaderRow-->
      <!--        </button>-->
      <!--      </li>-->
      <!--      <li class="text-xs">-->
      <!--        <button-->
      <!--          class="p-2 rounded-md border border-gray-300"-->
      <!--          @click="editor.commands.toggleHeaderCell()"-->
      <!--        >-->
      <!--          <font-awesome-icon icon="fa-solid fa-table" class="mr-1" />HeaderCell-->
      <!--        </button>-->
      <!--      </li>-->
      <!--      <li class="text-xs">-->
      <!--        <button-->
      <!--          class="p-2 rounded-md border border-gray-300"-->
      <!--          @click="editor.commands.mergeOrSplit()"-->
      <!--        >-->
      <!--          <font-awesome-icon-->
      <!--            icon="fa-solid fa-table"-->
      <!--            class="mr-1"-->
      <!--          />MergeOrSplit-->
      <!--        </button>-->
      <!--      </li>-->
    </ul>
  </div>
</template>

<style></style>
