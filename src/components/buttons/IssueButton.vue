<script setup>
import { ref, onMounted, onUnmounted } from 'vue';

// 버튼 상태 관리
const isOpen = ref(false);

// 버튼 토글
const toggleMenu = () => {
  isOpen.value = !isOpen.value;
};

// 외부 클릭 시 메뉴 닫기
const closeMenu = event => {
  if (!event.target.closest('.issue-menu')) {
    isOpen.value = false;
  }
};

onMounted(() => {
  document.addEventListener('click', closeMenu);
});

onUnmounted(() => {
  document.removeEventListener('click', closeMenu);
});
</script>

<template>
  <div
    class="issue-menu fixed bottom-8 right-8 flex flex-col items-end space-y-4"
  >
    <!-- 버튼 그룹 (애니메이션 적용) -->
    <transition-group name="slide">
      <a
        v-if="isOpen"
        href="https://github.com/nogi-org/nogi/issues/new?template=기능-개선-요청.md"
        target="_blank"
        class="issue-open-button border-gray-500 bg-gray-700 hover:bg-gray-600"
      >
        🚀 기능 개선 요청
      </a>
      <a
        v-if="isOpen"
        href="https://github.com/nogi-org/nogi/issues/new?template=문의-사항.md"
        target="_blank"
        class="issue-open-button border-gray-600 bg-gray-800 hover:bg-gray-700"
      >
        ❓ 문의 사항
      </a>
      <a
        v-if="isOpen"
        href="https://github.com/nogi-org/nogi/issues/new?template=버그-신고.md"
        target="_blank"
        class="issue-open-button border-gray-700 bg-gray-900 hover:bg-gray-800"
      >
        🐞 버그 신고
      </a>
    </transition-group>

    <!-- 플로팅 버튼 -->
    <button
      @click="toggleMenu"
      class="flex items-center justify-center text-white rounded-full shadow-lg bg-gray-900 hover:bg-gray-800 transition-all w-[60px] h-[60px] md:w-[80px] md:h-[80px] text-2xl"
    >
      {{ isOpen ? '✖' : '📝' }}
    </button>
  </div>
</template>

<style scoped>
/* 애니메이션 적용 */
.slide-enter-active,
.slide-leave-active {
  transition: all 0.3s ease-in-out;
}

.slide-enter-from,
.slide-leave-to {
  opacity: 0;
  transform: translateY(20px);
}

.issue-open-button {
  @apply w-[180px] md:w-[210px] text-white  text-center py-5 px-8 rounded-xl shadow-lg border transition-all text-sm md:text-lg;
}
</style>
