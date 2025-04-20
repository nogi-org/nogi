<script setup></script>

<template>
  <footer class="py-5 text-xs">
    <ul class="flex gap-3 text-xs justify-center">
      <li>
        <router-link :to="{ name: 'home' }">사이트 소개</router-link>
      </li>
      <li>
        <router-link :to="{ name: 'usePolicy' }">이용정책</router-link>
      </li>
    </ul>
    <p class="font-noto_sans_m text-center">© 2025 superpil / 오원택</p>
  </footer>
</template>
