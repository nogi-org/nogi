<script setup>
import SMainTitle from '@/shared/title/SMainTitle.vue';
import { onMounted } from 'vue';
import { UserManager } from '@/manager/user/UserManager.js';
import { NotionManager } from '@/manager/notion/NotionManager.js';
import SActionButton from '@/shared/buttons/SActionButton.vue';
import router from '@/router/router.js';

const user = new UserManager();
const notion = new NotionManager();
onMounted(async () => {
  await user.getUsers();
});

const goToNotionDatabase = user => {
  router.push({ name: 'userNotionDatabase', params: { userId: user.id } });
};

const goToNotionPages = user => {
  router.push({ name: 'userNotionPages', params: { userId: user.id } });
};
</script>

<template>
  <div>
    <SMainTitle title="Action" />
    <div class="border border-main p-4 mb-10 flex gap-3">
      <button
        class="bg-action px-3 py-1 rounded-md"
        @click="notion.updateNotionPageId()"
      >
        Notion Page ID 업데이트
      </button>
      <button
        class="bg-action px-3 py-1 rounded-md"
        @click="notion.updateNotionPageId()"
      >
        Notion Page 속성 추가(개발 필요)
      </button>
    </div>

    <SMainTitle :title="`User (${user.users.value.length})`" />
    <div class="relative overflow-x-auto rounded-md">
      <table class="w-full text-sm text-left text-neutral">
        <thead class="text-xs uppercase bg-main font-noto_sans_m">
          <tr>
            <th class="px-6 py-3" scope="col">Role</th>
            <th class="px-6 py-3" scope="col">Owner</th>
            <th class="px-6 py-3" scope="col">Email</th>
            <th class="px-6 py-3" scope="col">Git ID</th>
            <th class="px-6 py-3" scope="col">Repository</th>
            <th class="px-6 py-3" scope="col">Branch</th>
            <th class="px-6 py-3" scope="col">Page ID</th>
            <th class="px-6 py-3" scope="col">Git Token</th>
            <th class="px-6 py-3" scope="col">Notion Token</th>
            <th class="px-6 py-3" scope="col">Notion Database</th>
          </tr>
        </thead>
        <tbody>
          <template v-for="(item, index) in user.users.value" :key="index">
            <tr>
              <td class="px-6 py-4">{{ item.role }}</td>
              <td class="px-6 py-4 font-noto_sans_b">{{ item.owner }}</td>
              <td class="px-6 py-4 whitespace-nowrap">{{ item.email }}</td>
              <td class="px-6 py-4">{{ item.githubId }}</td>
              <td class="px-6 py-4 whitespace-nowrap">{{ item.repository }}</td>
              <td class="px-6 py-4">{{ item.branch }}</td>
              <td class="px-6 py-4 whitespace-nowrap">{{ item.pageId }}</td>
              <td
                :class="item.hasGithubToken ? 'text-action' : 'text-danger'"
                class="px-6 py-4"
              >
                {{ item.hasGithubToken }}
              </td>
              <td
                :class="item.hasNotionToken ? 'text-action' : 'text-danger'"
                class="px-6 py-4"
              >
                {{ item.hasNotionToken }}
              </td>
              <td
                :class="
                  item.hasNotionDatabaseId ? 'text-action' : 'text-danger'
                "
                class="px-6 py-4"
              >
                {{ item.hasNotionDatabaseId }}
              </td>
            </tr>
            <tr class="border-b border-neutral border-dashed">
              <td colspan="10" class="px-6 py-2">
                <div class="flex gap-3">
                  <SActionButton
                    name="Notion Database"
                    @action="goToNotionDatabase(item)"
                  />
                  <SActionButton
                    name="Notion Pages"
                    @action="goToNotionPages(item)"
                  />
                </div>
              </td>
            </tr>
          </template>
        </tbody>
      </table>
    </div>
  </div>
</template>
