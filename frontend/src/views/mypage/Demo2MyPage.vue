<template>
  <div
    class="relative flex flex-col items-center justify-center min-h-screen bg-gradient-to-b"
  >
    <!-- 로켓 애니메이션 -->
    <div class="animate-rocket text-6xl">🚀</div>

    <h1 class="text-4xl md:text-5xl font-extrabold mt-6 text-center">
      다음 버전에서 새로운 기능을 만나보세요! 🎉
    </h1>
    <p class="mt-4 text-lg text-center max-w-2xl">
      더욱 편리하고 강력한 기능으로 돌아옵니다. 새로운 업데이트를 확인하세요!
    </p>

    <!-- 새로운 기능 목록 -->
    <div
      class="mt-8 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 max-w-4xl"
    >
      <div class="flex items-center p-4 bg-gray-800 rounded-lg shadow-md">
        <span class="text-3xl">💡</span>
        <div class="ml-4">
          <h3 class="text-xl font-semibold">스마트 AI 추천</h3>
          <p class="text-sm text-gray-300">
            사용자의 취향을 분석하여 최적의 컨텐츠를 추천합니다.
          </p>
        </div>
      </div>
      <div class="flex items-center p-4 bg-gray-800 rounded-lg shadow-md">
        <span class="text-3xl">⚡</span>
        <div class="ml-4">
          <h3 class="text-xl font-semibold">더 빠른 속도</h3>
          <p class="text-sm text-gray-300">
            최적화된 성능으로 더욱 빠른 로딩 속도를 경험하세요.
          </p>
        </div>
      </div>
      <div class="flex items-center p-4 bg-gray-800 rounded-lg shadow-md">
        <span class="text-3xl">🔒</span>
        <div class="ml-4">
          <h3 class="text-xl font-semibold">강력한 보안</h3>
          <p class="text-sm text-gray-300">
            데이터 암호화 및 보안 기능이 강화되었습니다.
          </p>
        </div>
      </div>
      <div class="flex items-center p-4 bg-gray-800 rounded-lg shadow-md">
        <span class="text-3xl">🎨</span>
        <div class="ml-4">
          <h3 class="text-xl font-semibold">새로운 UI 디자인</h3>
          <p class="text-sm text-gray-300">
            더 직관적이고 세련된 디자인을 제공합니다.
          </p>
        </div>
      </div>
      <div class="flex items-center p-4 bg-gray-800 rounded-lg shadow-md">
        <span class="text-3xl">📱</span>
        <div class="ml-4">
          <h3 class="text-xl font-semibold">모바일 최적화</h3>
          <p class="text-sm text-gray-300">
            모든 디바이스에서 완벽한 사용 경험을 제공합니다.
          </p>
        </div>
      </div>
      <div class="flex items-center p-4 bg-gray-800 rounded-lg shadow-md">
        <span class="text-3xl">🌍</span>
        <div class="ml-4">
          <h3 class="text-xl font-semibold">다국어 지원</h3>
          <p class="text-sm text-gray-300">전 세계 다양한 언어를 지원합니다.</p>
        </div>
      </div>
    </div>
  </div>
</template>

<style>
/* 로켓 애니메이션 */
@keyframes rocketMove {
  0%,
  100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}
.animate-rocket {
  animation: rocketMove 1.5s infinite ease-in-out;
}

/* 반짝이는 별 애니메이션 */
@keyframes twinkle1 {
  0%,
  100% {
    opacity: 0.3;
    transform: scale(1);
  }
  50% {
    opacity: 1;
    transform: scale(1.2);
  }
}
@keyframes twinkle2 {
  0%,
  100% {
    opacity: 0.2;
    transform: scale(0.9);
  }
  50% {
    opacity: 1;
    transform: scale(1.1);
  }
}
@keyframes twinkle3 {
  0%,
  100% {
    opacity: 0.4;
    transform: scale(1);
  }
  50% {
    opacity: 1;
    transform: scale(1.3);
  }
}
.animate-twinkle1 {
  animation: twinkle1 1.5s infinite alternate;
}
.animate-twinkle2 {
  animation: twinkle2 1.7s infinite alternate;
}
.animate-twinkle3 {
  animation: twinkle3 2s infinite alternate;
}
</style>
<script setup lang="ts"></script>
