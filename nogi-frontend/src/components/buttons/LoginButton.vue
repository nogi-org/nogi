<script setup>
const emit = defineEmits(['onLogin']);

const onLogin = () => {
  emit('onLogin');
};
</script>

<template>
  <div>
    <button
      class="text-white bg-main font-noto_sans_r rounded-md px-2.5 py-2 text-center inline-flex items-center text-xs"
      type="button"
      @click="onLogin"
    >
      로그인 / 회원가입
    </button>
  </div>
</template>

<style scoped></style>
