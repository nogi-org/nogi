<script setup>
import { onBeforeMount, ref } from 'vue';

const recentPosts = ref([]);
const popularPosts = ref([]);
const heartStoryPosts = ref([]);

onBeforeMount(async () => {});
</script>

<template>
  <div class="text-white w-full">
    <!-- Hero Section -->
    <section class="w-full bg-gradient-to-r from-blue-700 to-indigo-800">
      <div class="max-w-[1280px] mx-auto px-4 md:px-8 py-20 text-center">
        <h1 class="text-4xl md:text-5xl font-bold">
          Notion에서 기록하면,
          <span class="text-yellow-300">자동으로 GitHub에 커밋!</span> 🚀
        </h1>
        <p class="mt-4 text-lg md:text-xl">
          공부한 내용을 쉽게 관리하고,
          <span class="font-bold text-yellow-300"
            >GitHub Contributions(🌱 잔디)</span
          >도 채우세요!
        </p>
        <router-link to="/user-guide">
          <button
            class="mt-6 bg-white text-blue-600 font-semibold py-2 px-6 rounded-lg shadow-lg hover:bg-gray-100"
          >
            시작하기 →
          </button>
        </router-link>
      </div>
    </section>

    <!-- 기존 방식의 문제점 vs 해결책 -->
    <section class="w-full">
      <div class="max-w-[1280px] mx-auto px-4 md:px-8 py-16">
        <div class="grid md:grid-cols-2 gap-12">
          <div class="bg-gray-800 p-8 rounded-xl shadow-lg">
            <h2 class="text-2xl font-bold flex items-center">
              <span class="text-red-500 text-3xl mr-2">📌</span> 기존 방식의
              문제점
            </h2>
            <p class="mt-4 text-gray-300">
              Notion에 공부 내용을 정리하지만, GitHub에 올리는 과정이
              번거롭다.<br />
              <span class="text-yellow-300 font-bold">GitHub 🌱 잔디</span>를
              꾸준히 채우고 싶지만, 매번 직접 커밋하기 귀찮다.<br />
              학습 기록을 한 곳에서 체계적으로 관리하고 싶다.
            </p>
          </div>
          <div class="bg-green-600 p-8 rounded-xl shadow-lg">
            <h2 class="text-2xl font-bold flex items-center">
              <span class="text-white text-3xl mr-2">✅</span> 우리의 해결책
            </h2>
            <p class="mt-4 text-white">
              Notion에서 작성하면 자동으로 GitHub에 커밋 →
              <span class="font-bold text-yellow-300">100% 자동화!</span><br />
              <span class="font-bold text-yellow-300">커밋 예약 기능</span> →
              원하는 날짜에 자동으로 업로드!<br />
              <span class="font-bold text-yellow-300">마크다운 & 이미지</span
              >까지 깔끔하게 저장!
            </p>
          </div>
        </div>
      </div>
    </section>

    <!-- 🌟 Notion → GitHub 과정 이미지 추가 -->
    <section class="w-full">
      <div class="max-w-[1280px] mx-auto px-4 md:px-8 py-16 text-center">
        <h2 class="text-3xl font-bold">
          📝 Notion에서 작성한 글이 자동으로 GitHub에!
        </h2>
        <p class="mt-4 text-lg text-gray-300">
          Notion에서 글을 작성하면 자동으로 GitHub에 업로드됩니다! 마크다운 형식
          & 이미지를 포함한 자동 연동
        </p>
        <div class="flex justify-center items-center mt-8 flex-col md:flex-row">
          <!-- Notion 이미지 -->
          <div class="w-full md:max-w-[45%] h-auto">
            <img src="/assets/images/notion_til.png" />
            <p class="text-gray-400 text-sm md:text-base mt-2">
              📌 Notion에 작성한 학습 내용
            </p>
          </div>

          <!-- 화살표 (데스크톱: →, 모바일: ↓) -->
          <span class="text-4xl mx-4 text-gray-300 hidden md:block">→</span>
          <span class="text-3xl my-4 text-gray-300 md:hidden">↓</span>

          <!-- GitHub 이미지 -->
          <div class="w-full md:max-w-[45%] h-auto">
            <img src="/assets/images/github_til.png" />
            <p class="text-gray-400 text-sm md:text-base mt-2">
              📤 GitHub에 자동 업로드된 기록
            </p>
          </div>
        </div>
      </div>
    </section>

    <!-- 주요 기능 -->
    <section class="w-full">
      <div class="max-w-[1280px] mx-auto px-4 md:px-8 py-16">
        <h2 class="text-3xl font-bold text-center">✨ 주요 기능</h2>
        <div class="grid md:grid-cols-3 gap-8 mt-8">
          <div class="bg-gray-800 p-6 rounded-lg shadow-lg">
            <h3 class="text-xl font-semibold text-blue-400">
              📌 Notion → GitHub 자동 연동
            </h3>
            <p class="mt-2 text-gray-300">
              Notion에서 작성한 글이 GitHub에 Markdown 형태로 저장됩니다.
            </p>
          </div>
          <div class="bg-gray-800 p-6 rounded-lg shadow-lg">
            <h3 class="text-xl font-semibold text-green-400">
              📅 <span class="font-bold text-yellow-300">커밋 예약 기능</span>
            </h3>
            <p class="mt-2 text-gray-300">
              원하는 날짜에 자동으로 GitHub에 업로드할 수 있습니다.
            </p>
          </div>
          <div class="bg-gray-800 p-6 rounded-lg shadow-lg">
            <h3 class="text-xl font-semibold text-yellow-400">
              🖼️ <span class="font-bold text-yellow-300">이미지 저장 지원</span>
            </h3>
            <p class="mt-2 text-gray-300">
              Notion에서 업로드한 이미지도 함께 저장됩니다.
            </p>
          </div>
        </div>
      </div>
    </section>

    <!-- 사용 사례 -->
    <section class="w-full">
      <div class="max-w-[1280px] mx-auto px-4 md:px-8 py-16">
        <h2 class="text-3xl font-bold text-center">🎯 사용 사례 & 기대 효과</h2>
        <div class="grid md:grid-cols-3 gap-8 mt-8">
          <div class="bg-gray-800 p-6 rounded-lg shadow-lg">
            <h3 class="text-xl font-semibold text-indigo-400">
              📖 개발 공부 기록 자동화
            </h3>
            <p class="mt-2 text-gray-300">
              매일 Notion에 학습 내용을 정리하면, 자동으로 GitHub에
              업로드됩니다.
            </p>
          </div>
          <div class="bg-gray-800 p-6 rounded-lg shadow-lg">
            <h3 class="text-xl font-semibold text-pink-400">
              🌿 GitHub Contributions 유지
            </h3>
            <p class="mt-2 text-gray-300">
              GitHub 🌱 잔디를 꾸준히 채우고, 개발 활동을 기록할 수 있습니다.
            </p>
          </div>
          <div class="bg-gray-800 p-6 rounded-lg shadow-lg">
            <h3 class="text-xl font-semibold text-orange-400">
              📂 개발 블로그처럼 활용
            </h3>
            <p class="mt-2 text-gray-300">
              Notion에서 작성한 글이 GitHub 블로그처럼 관리됩니다.
            </p>
          </div>
        </div>
      </div>
    </section>

    <!-- Call to Action -->
    <section class="w-full bg-gradient-to-r from-purple-800 to-blue-700">
      <div class="max-w-[1280px] mx-auto px-4 md:px-8 py-20 text-center">
        <h2 class="text-4xl font-bold">🚀 지금 바로 시작하세요!</h2>
        <p class="mt-4 text-lg">
          무료로 Notion과 GitHub을 연동하고 자동 커밋을 경험해보세요!
        </p>
        <router-link to="/user-guide">
          <button
            class="mt-6 bg-white text-purple-600 font-semibold py-2 px-6 rounded-lg shadow-lg hover:bg-gray-100"
          >
            가이드 바로가기 →
          </button>
        </router-link>
      </div>
    </section>

    <!-- GitHub 저장소 소개 및 Star 유도 -->
    <section class="w-full bg-gray-900">
      <div class="max-w-[1280px] mx-auto px-4 md:px-8 py-20 text-center">
        <h2 class="text-4xl font-bold text-white">
          💡 이 프로젝트가 유용하다고 생각하신다면?
        </h2>
        <p class="mt-4 text-lg text-gray-300">
          작은 응원이 큰 도움이 됩니다! ⭐️ Star를 눌러주세요.
        </p>

        <!-- GitHub 버튼 영역 -->
        <div class="flex justify-center mt-6 space-x-4">
          <a
            href="https://github.com/nogi-org/nogi"
            target="_blank"
            class="bg-yellow-400 text-black font-semibold py-3 px-6 rounded-lg shadow-lg hover:bg-yellow-500 transition"
          >
            ⭐ GitHub에서 Star 주기
          </a>
        </div>
      </div>
    </section>
  </div>
</template>

<style scoped>
.bg-dark {
  background-color: #0a0a0a;
}

.bg-dark-800 {
  background-color: #111827;
}

.w-full {
  width: 100%;
}
</style>
