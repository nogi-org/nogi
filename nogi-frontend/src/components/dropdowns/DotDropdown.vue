<script setup>
import { onMounted } from 'vue';
import { initFlowbite } from 'flowbite';

defineProps({
  id: {
    type: Number
  },
  menu: {
    type: Array
  }
});
const emit = defineEmits(['action']);

onMounted(() => {
  initFlowbite();
});

const action = (item, id) => {
  emit('action', { id, ...item });
};
</script>

<template>
  <div>
    <button
      :id="id ? `dropdownMenuIconHorizontalButton${id}` : 'dropdownMenuIconHorizontalButton'"
      :data-dropdown-toggle="id ? `dropdownDotsHorizontal${id}` : 'dropdownDotsHorizontal'"
      class="inline-flex items-center p-2 text-sm font-medium text-center text-gray-900"
      type="button"
    >
      <svg
        class="w-5 h-5"
        aria-hidden="true"
        xmlns="http://www.w3.org/2000/svg"
        fill="currentColor"
        viewBox="0 0 16 3"
      >
        <path
          d="M2 0a1.5 1.5 0 1 1 0 3 1.5 1.5 0 0 1 0-3Zm6.041 0a1.5 1.5 0 1 1 0 3 1.5 1.5 0 0 1 0-3ZM14 0a1.5 1.5 0 1 1 0 3 1.5 1.5 0 0 1 0-3Z"
        />
      </svg>
    </button>

    <!-- Dropdown menu -->
    <div
      :id="id ? `dropdownDotsHorizontal${id}` : 'dropdownDotsHorizontal'"
      class="z-10 hidden bg-white divide-y divide-gray-100 rounded-lg shadow w-44 dark:bg-gray-700 dark:divide-gray-600"
    >
      <ul class="py-2 text-sm text-gray-700" aria-labelledby="dropdownMenuIconHorizontalButton">
        <li v-for="item in menu">
          <button class="text-left w-full px-4 py-2 hover:bg-gray-100" @click="action(item, id)">
            {{ item.name }}
          </button>
        </li>
      </ul>
    </div>
  </div>
</template>
