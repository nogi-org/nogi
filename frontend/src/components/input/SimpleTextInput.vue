<script setup>
import { computed } from 'vue';

const props = defineProps({
  placeholder: {
    type: String,
    default: 'text 입력창'
  },
  disabled: {
    type: Boolean,
    default: false
  },
  value: {
    type: String,
    default: null
  }
});

const emit = defineEmits(['update:value']);
const modelValue = defineModel();

// `value`가 존재하면 사용하고, 없으면 `modelValue`를 사용하는 computed 속성
const inputValue = computed({
  get: () => props.value ?? modelValue.value,
  set: (newValue) => {
    if (props.value !== null) {
      emit('update:value', newValue);
    } else {
      modelValue.value = newValue;
    }
  }
});
</script>

<template>
  <div>
    <input
      v-model="inputValue"
      type="text"
      :placeholder="placeholder"
      :disabled="disabled"
      class="sm:text-base text-sm rounded-md text-white w-full bg-transparent outline-none placeholder-neutral disabled:cursor-not-allowed disabled:text-neutral placeholder:text-xs"
    />
  </div>
</template>
