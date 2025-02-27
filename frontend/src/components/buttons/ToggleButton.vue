<script setup>
import { ref } from 'vue';

const props = defineProps({
  buttons: {
    type: Array
  }
});
const activeIndex = ref(props.buttons.findIndex(button => button.default === true));
const emit = defineEmits(['activeButton']);

const activeButton = (item, index) => {
  activeIndex.value = index;
  emit('activeButton', { ...item, index: index });
};
</script>

<template>
  <div>
    <ul class="flex">
      <li class="mr-3" v-for="(item, index) in buttons">
        <button
          class="border-main border px-4 py-1 rounded-2xl text-sm"
          :class="{ 'bg-gradient-to-r from-indigo-500 via-purple-500 to-pink-500 text-white font-noto_sans_m': activeIndex === index }"
          @click="activeButton(item, index)"
        >
          {{ item.name }}
        </button>
      </li>
    </ul>
  </div>
</template>
