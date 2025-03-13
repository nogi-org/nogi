<template>
  <div class="flex justify-center gap-4">
    <template v-if="beginPage !== 1">
      <!-- 맨앞 -->
      <button
        class="items-center text-xs sm:text-sm"
        @click.prevent="$emit('changePage', toPage(1), 1)"
      >
        <font-awesome-icon icon="fa-solid fa-angles-left" />
      </button>

      <!-- 이전 -->
      <button
        class="items-center text-xs sm:text-sm"
        @click.prevent="
          $emit('changePage', toPage(beginPage - 1), beginPage - 1)
        "
      >
        <font-awesome-icon icon="fa-solid fa-angle-left" />
      </button>
    </template>

    <!--  페이지  -->
    <span v-for="index in pageRange" :key="index">
      <button
        class="text-xs text-main bg-violet-100 py-2 px-3 rounded-md font-noto_sans_m sm:text-sm"
        v-if="pageNo === index"
      >
        {{ index }}
      </button>
      <button
        class="sm:text-sm items-center text-xs py-2 px-3 rounded-sm"
        v-else
        :key="index"
        @click.prevent="$emit('changePage', toPage(index), index)"
      >
        {{ index }}
      </button>
    </span>

    <template v-if="endPage !== totalPage">
      <!-- 다음 -->
      <button
        class="items-center text-xs sm:text-sm"
        @click.prevent="$emit('changePage', toPage(endPage + 1), endPage + 1)"
      >
        <font-awesome-icon icon="fa-solid fa-angle-right" />
      </button>

      <!-- 맨끝 -->
      <button
        class="items-center text-xs sm:text-sm"
        @click.prevent="$emit('changePage', toPage(totalPage), totalPage)"
      >
        <font-awesome-icon icon="fa-solid fa-angles-right" />
      </button>
    </template>
  </div>
</template>

<script>
export default {
  props: {
    total: {
      type: Number,
      default: 0
    },
    page: {
      type: Number,
      default: 0
    },
    size: {
      type: Number,
      default: 2
    },
    pageRangeSize: {
      type: Number,
      default: 5
    }
  },
  computed: {
    beginPage: function () {
      return this.pageIndex * this.pageRangeSize + 1;
    },
    endPage: function () {
      return Math.min(
        (this.pageIndex + 1) * this.pageRangeSize,
        this.totalPage
      );
    },
    pageIndex: function () {
      return parseInt((this.pageNo - 1) / this.pageRangeSize);
    },
    totalPage: function () {
      return Math.ceil(this.total / this.size);
    },
    pageNo: function () {
      return this.page + 1;
    },
    pageRange: function () {
      let ret = [];
      for (let i = this.beginPage; i <= this.endPage; ++i) ret.push(i);
      return ret;
    }
  },
  methods: {
    toPage: function (pageNo) {
      return pageNo - 1;
    }
  }
};
</script>
