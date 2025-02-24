<script setup>
import { toYMDHMs } from '@/utils/dateFormat.js';

defineProps({
  views: {
    type: Number,
    default: 0
  },
  createDate: {
    type: String,
    default: '2024.01.01'
  }
});
</script>

<template>
  <div>
    <span class="text-gray-400 mr-5 text-xs"><font-awesome-icon icon="fa-regular fa-eye" class="mr-1" />{{ views.toLocaleString() }}</span>
    <span class="text-gray-400 text-xs"> <font-awesome-icon icon="fa-regular fa-clock" class="mr-1" />{{ toYMDHMs(createDate) }} </span>
  </div>
</template>
