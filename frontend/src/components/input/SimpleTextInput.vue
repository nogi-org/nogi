<script setup>
defineProps({
  placeholder: {
    type: String,
    default: 'text 입력창'
  },
  disabled: {
    type: Boolean,
    default: false
  }
});
const modelValue = defineModel();
</script>

<template>
  <div>
    <input
      v-model="modelValue"
      type="text"
      :placeholder="placeholder"
      :disabled="disabled"
      class="rounded-md text-white w-full bg-transparent outline-none placeholder-neutral disabled:cursor-not-allowed disabled:text-neutral"
    />
  </div>
</template>
