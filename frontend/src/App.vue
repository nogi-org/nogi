<script setup>
import { RouterView } from 'vue-router';
import SystemChecking from '@/components/common/SystemChecking.vue';

const SYSTEM_CHECKING = true;
</script>

<template>
  <SystemChecking v-if="SYSTEM_CHECKING" />
  <RouterView v-else />
</template>
