<script setup>
import { RouterView } from 'vue-router';
import SSystemChecking from '@/shared/common/SSystemChecking.vue';

const SYSTEM_CHECKING = false;
</script>

<template>
  <SSystemChecking v-if="SYSTEM_CHECKING" />
  <RouterView v-else />
</template>
