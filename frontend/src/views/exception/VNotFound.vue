<script setup></script>

<template>
  <div
    class="absolute left-1/2 top-1/2 transform -translate-x-1/2 -translate-y-1/2 text-center"
  >
    <h1 class="text-xl text-rose-500 font-noto_sans_r">OOPS!</h1>
    <h1 class="text-7xl my-5 font-noto_sans_r">404</h1>
    <router-link
      :to="{ name: 'home' }"
      class="px-2 py-1.5 border border-white rounded-sm animate-blink"
    >
      Home 이동
    </router-link>
  </div>
</template>

<style scoped></style>
