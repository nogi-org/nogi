<script setup>
import { computed } from 'vue';

const props = defineProps({
  isConnected: {
    type: Boolean,
    default: null
  }
});

// 상태를 computed 속성으로 분리
const connectionStatus = computed(() => {
  if (props.isConnected === null) return 'Checking...';
  return props.isConnected ? 'Connected' : 'Disconnected';
});
</script>

<template>
  <div
    class="font-noto_sans_m"
    :class="{
      'text-green-500': isConnected === true,
      'text-danger': isConnected === false,
      'text-neutral': isConnected === null
    }"
  >
    <font-awesome-icon
      icon="fa-solid fa-circle-check"
      v-if="isConnected"
      class="mr-1"
    />
    <font-awesome-icon
      icon="fa-solid fa-circle-xmark"
      v-if="isConnected === false"
      class="mr-1"
    />
    <font-awesome-icon
      v-if="isConnected === null"
      icon="fa-solid fa-spinner"
      class="mr-1 fa-spin"
    />
    <span>
      {{ connectionStatus }}
    </span>
  </div>
</template>

<style scoped></style>
