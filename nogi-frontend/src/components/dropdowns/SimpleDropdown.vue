<script setup>
import { Menu, MenuButton, MenuItem, MenuItems } from '@headlessui/vue';
import { ChevronDownIcon } from '@heroicons/vue/20/solid';
import { ref, watch } from 'vue';

const props = defineProps({
  list: {
    type: Array,
    required: true
  },
  defaultActiveTitle: {
    default: '카테고리'
  }
});
const emit = defineEmits(['select']);
const activeTitle = ref(props.defaultActiveTitle);

watch(
  () => props.defaultActiveTitle,
  newValue => {
    activeTitle.value = newValue.name;
  }
);
const select = category => {
  activeTitle.value = category.name;
  emit('select', category);
};
</script>

<template>
  <Menu as="div" class="relative inline-block text-left z-20 w-full">
    <div>
      <MenuButton
        class="inline-flex w-full justify-between items-center gap-x-1.5 rounded-md bg-white px-3 py-2 text-xs text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300"
      >
        {{ activeTitle }}
        <ChevronDownIcon class="-mr-1 h-5 w-5 text-gray-400 text-xs" aria-hidden="true" />
      </MenuButton>
    </div>

    <transition
      enter-active-class="transition ease-out duration-100"
      enter-from-class="transform opacity-0 scale-95"
      enter-to-class="transform opacity-100 scale-100"
      leave-active-class="transition ease-in duration-75"
      leave-from-class="transform opacity-100 scale-100"
      leave-to-class="transform opacity-0 scale-95"
    >
      <MenuItems
        class="w-full absolute left-0 z-10 mt-2 origin-top-right rounded-md bg-white shadow-lg"
      >
        <div class="py-1">
          <MenuItem v-slot="{ active }" v-for="item in list">
            <button
              :class="[
                active ? 'bg-gray-100 text-gray-900' : 'text-gray-700',
                'block px-4 py-2 text-sm w-full text-left'
              ]"
              @click="select(item)"
            >
              {{ item.name }}
            </button>
          </MenuItem>
        </div>
      </MenuItems>
    </transition>
  </Menu>
</template>
