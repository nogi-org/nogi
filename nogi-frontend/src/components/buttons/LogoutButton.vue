<script setup>
const emit = defineEmits(['onLogout']);

const onLogout = () => {
  emit('onLogout');
};
</script>

<template>
  <div>
    <button
      class="text-white bg-main font-noto_sans_r rounded-md px-2.5 py-2 text-center inline-flex items-center text-xs"
      type="button"
      @click="onLogout"
    >
      로그아웃
    </button>
  </div>
</template>

<style scoped></style>
