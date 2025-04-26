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
    :class="{
      'text-green-500': isConnected === true,
      'text-danger': isConnected === false,
      'text-neutral': isConnected === null
    }"
    class="font-noto_sans_m"
  >
    <font-awesome-icon
      v-if="isConnected"
      class="mr-1"
      icon="fa-solid fa-circle-check"
    />
    <font-awesome-icon
      v-if="isConnected === false"
      class="mr-1"
      icon="fa-solid fa-circle-xmark"
    />
    <font-awesome-icon
      v-if="isConnected === null"
      class="mr-1 fa-spin"
      icon="fa-solid fa-spinner"
    />
    <span>
      {{ connectionStatus }}
    </span>
  </div>
</template>

<style scoped></style>
