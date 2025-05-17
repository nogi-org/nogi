<script setup>
import { toYMDHMS } from '@/utils/dateFormat.js';

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
    <span class="text-gray-400 mr-5 text-xs"
      ><font-awesome-icon class="mr-1" icon="fa-regular fa-eye" />{{
        views.toLocaleString()
      }}</span
    >
    <span class="text-gray-400 text-xs">
      <font-awesome-icon class="mr-1" icon="fa-regular fa-clock" />{{
        toYMDHMS(createDate)
      }}
    </span>
  </div>
</template>
