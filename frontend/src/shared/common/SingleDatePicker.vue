<script setup>
import { onMounted, ref, watch } from 'vue';
const emit = defineEmits(['selectDate']);
const selectDate = ref('');
const dateFormat = date => {
  const day = String(date.getDate()).padStart(2, '0');
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const year = date.getFullYear();
  return `${year}-${month}-${day}`;
};

const displayDate = date => {
  return `현재 ~ ${dateFormat(date)} 까지 모집`;
};

onMounted(() => {
  selectDate.value = new Date();
});

watch(selectDate, newDate => {
  emit('selectDate', dateFormat(newDate));
});
</script>

<template>
  <VueDatePicker
    v-model="selectDate"
    :clearable="false"
    :enable-time-picker="false"
    :start-date="selectDate"
    :format="displayDate"
    :min-date="new Date()"
    position="left"
  />
</template>
