<script setup>
defineProps({
  isConnected: {
    type: Boolean,
    default: false
  }
});
</script>

<template>
  <div
    class="font-noto_sans_m"
    :class="isConnected ? 'text-green-500' : 'text-danger'"
  >
    <font-awesome-icon
      icon="fa-solid fa-circle-check"
      v-if="isConnected"
      class="mr-1"
    />
    <font-awesome-icon icon="fa-solid fa-circle-xmark" v-else class="mr-1" />
    <span>{{ isConnected ? 'Connected' : 'Disconnected' }}</span>
  </div>
</template>

<style scoped></style>
