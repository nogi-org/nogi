<script setup>
import SDangerButton from '@/shared/buttons/SDangerButton.vue';
import { inject } from 'vue';

const user = inject('userManager');
</script>

<template>
  <div class="mb-10">
    <h2 class="font-noto_sans_b text-lg">Danger Zone</h2>
    <hr class="border-main my-4" />
    <ul class="border border-danger rounded-sm p-4">
      <li class="flex justify-between items-center">
        <span class="text-sm">회원탈퇴</span>
        <SDangerButton name="Delete" @action="user.deleteUser()" />
      </li>
    </ul>
  </div>
</template>
