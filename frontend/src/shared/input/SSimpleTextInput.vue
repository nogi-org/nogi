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

const value = defineModel();
</script>

<template>
  <div>
    <input
      v-model="value"
      :disabled="disabled"
      :placeholder="placeholder"
      class="sm:text-base text-sm rounded-md text-white w-full bg-transparent outline-none placeholder-neutral disabled:cursor-not-allowed disabled:text-neutral placeholder:text-xs"
      type="text"
    />
  </div>
</template>
