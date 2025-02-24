<script setup></script>

<template>
  <div class="text-gray-700 text-xs px-2 py-1 font-noto_sans_r">
    <input
      id="checked-checkbox"
      type="checkbox"
      value=""
      class="w-4 h-4 text-main bg-gray-100 border-gray-300 rounded cursor-pointer focus:ring-0"
    />
    <label for="checked-checkbox" class="cursor-pointer text-xs"> [필수]이용약관 동의 </label>
    <router-link :to="{ name: 'usePolicy' }" class="text-main">[보기]</router-link>
  </div>
</template>
