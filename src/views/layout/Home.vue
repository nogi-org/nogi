<script setup>
import { onBeforeMount, ref } from 'vue';

const recentPosts = ref([]);
const popularPosts = ref([]);
const heartStoryPosts = ref([]);

onBeforeMount(async () => {});
</script>

<template>
  <main>
    home
    <!--    <div class="mb-28 md:flex">-->
    <!--      <div class="mb-10 md:mb-0 md:w-2/3">-->
    <!--        <p class="font-noto_sans_l py-2 sm:text-xl md:text-2xl lg:text-3xl">-->
    <!--          정보를 나누고, 꿈을 이루는 커뮤니티-->
    <!--        </p>-->
    <!--        <p class="font-noto_sans_b sm:text-xl md:text-2xl lg:text-3xl">-->
    <!--          해외취업 가이드 🚀-->
    <!--        </p>-->
    <!--      </div>-->
    <!--      &lt;!&ndash;인기 자유잡담&ndash;&gt;-->
    <!--      <div class="px-4 py-5 rounded-lg md:w-1/3">-->
    <!--        <div class="flex justify-between mb-7">-->
    <!--          <h2 class="text-lg font-noto_sans_r">-->
    <!--            <font-awesome-icon-->
    <!--              icon="fa-solid fa-fire"-->
    <!--              class="text-red-400 mr-1"-->
    <!--            />-->
    <!--            인기 이야기-->
    <!--          </h2>-->
    <!--          <div>-->
    <!--            <router-link :to="{ name: 'storiesPage' }">-->
    <!--              <font-awesome-icon-->
    <!--                icon="fa-solid fa-angle-right"-->
    <!--                class="ml-1.5 text-xs text-main font-noto_sans_m"-->
    <!--              />-->
    <!--            </router-link>-->
    <!--          </div>-->
    <!--        </div>-->
    <!--        <TalkShortInfoCard-->
    <!--          :talks="heartStoryPosts"-->
    <!--          @toDetailPage="toStoryDetailPage"-->
    <!--        />-->
    <!--      </div>-->
    <!--    </div>-->

    <!--    <ul>-->
    <!--      &lt;!&ndash;최신취업정보&ndash;&gt;-->
    <!--      <li>-->
    <!--        <div class="flex justify-between">-->
    <!--          <h2 class="text-xl mb-7 font-noto_sans_r sm:text-2xl">-->
    <!--            <font-awesome-icon-->
    <!--              icon="fa-solid fa-star"-->
    <!--              class="mr-1 text-yellow-400"-->
    <!--            />-->
    <!--            최신 취업정보-->
    <!--          </h2>-->
    <!--          <div>-->
    <!--            <RouterLink :to="{ name: 'userGuidePage' }">-->
    <!--              <span class="text-xs text-main font-noto_sans_m">더보기</span>-->
    <!--              <font-awesome-icon-->
    <!--                icon="fa-solid fa-angle-right"-->
    <!--                class="ml-1.5 text-xs text-main font-noto_sans_m"-->
    <!--              />-->
    <!--            </RouterLink>-->
    <!--          </div>-->
    <!--        </div>-->
    <!--        <ul class="flex-wrap sm:flex">-->
    <!--          <li class="mb-16 sm:w-[33.3333%] px-2" v-for="item in recentPosts">-->
    <!--            <JobInfoShortCard :info="item" @toDetailPage="toDetailPage" />-->
    <!--          </li>-->
    <!--        </ul>-->
    <!--      </li>-->
    <!--      &lt;!&ndash;인기취업정보&ndash;&gt;-->
    <!--      <li>-->
    <!--        <div class="flex justify-between">-->
    <!--          <h2 class="text-xl mb-7 font-noto_sans_r sm:text-2xl">-->
    <!--            <font-awesome-icon-->
    <!--              icon="fa-solid fa-fire-flame-curved"-->
    <!--              class="mr-1 text-main"-->
    <!--            />-->
    <!--            인기 취업정보-->
    <!--          </h2>-->
    <!--          <div>-->
    <!--            <RouterLink :to="{ name: 'userGuidePage' }">-->
    <!--              <span class="text-xs text-main font-noto_sans_m">더보기</span>-->
    <!--              <font-awesome-icon-->
    <!--                icon="fa-solid fa-angle-right"-->
    <!--                class="ml-1.5 text-xs text-main font-noto_sans_m"-->
    <!--              />-->
    <!--            </RouterLink>-->
    <!--          </div>-->
    <!--        </div>-->
    <!--        <ul class="flex-wrap sm:flex">-->
    <!--          <li class="mb-16 sm:w-[33.3333%] px-2" v-for="item in popularPosts">-->
    <!--            <JobInfoShortCard :info="item" @toDetailPage="toDetailPage" />-->
    <!--          </li>-->
    <!--        </ul>-->
    <!--      </li>-->
    <!--    </ul>-->
  </main>
</template>
