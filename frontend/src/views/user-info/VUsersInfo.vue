<script setup>
import CSettingTitle from '@/views/setting/components/CSettingTitle.vue';
import { onMounted } from 'vue';
import router from '@/router/router.js';
import { UserManager } from '@/manager/user/UserManager.js';
import { NotionManager } from '@/manager/notion/NotionManager.js';

const user = new UserManager();
const notion = new NotionManager();
onMounted(async () => {
  await user.getUsers();
});

const goToUserNotionPages = user => {
  console.log('user : ', user);
  router.push({ name: 'userNotionPages', params: { userId: user.id } });
};
</script>

<template>
  <div>
    <CSettingTitle title="Action" />
    <div class="border border-main p-4 mb-10">
      <button
        class="bg-action px-3 py-1 rounded-md"
        @click="notion.updateNotionPageId()"
      >
        모든 유저 Notion Page ID 업데이트
      </button>
    </div>

    <CSettingTitle :title="`User (${user.users.value.length})`" />
    <div class="relative overflow-x-auto rounded-md">
      <table class="w-full text-sm text-left text-neutral">
        <thead class="text-xs uppercase bg-main font-noto_sans_m">
          <tr>
            <th class="px-6 py-3 rounded-tl-md rounded-bl-md" scope="col">
              Git ID
            </th>
            <th class="px-6 py-3" scope="col">Role</th>
            <th class="px-6 py-3" scope="col">Owner</th>
            <th class="px-6 py-3" scope="col">Email</th>
            <th class="px-6 py-3" scope="col">Repository</th>
            <th class="px-6 py-3" scope="col">Branch</th>
            <th class="px-6 py-3" scope="col">Page ID</th>
            <th class="px-6 py-3" scope="col">Git Token</th>
            <th class="px-6 py-3" scope="col">Notion Token</th>
            <th class="px-6 py-3 rounded-tr-md rounded-br-md" scope="col">
              Notion Database
            </th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="(item, index) in user.users.value"
            :key="index"
            class="cursor-pointer hover:bg-main"
            @click="goToUserNotionPages(item)"
          >
            <td class="px-6 py-4 rounded-tl-md rounded-bl-md">
              {{ item.githubId }}
            </td>
            <td class="px-6 py-4">{{ item.role }}</td>
            <td class="px-6 py-4">{{ item.owner }}</td>
            <td class="px-6 py-4 whitespace-nowrap">{{ item.email }}</td>
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
              :class="item.hasNotionDatabaseId ? 'text-action' : 'text-danger'"
              class="px-6 py-4 rounded-tr-md rounded-br-md"
            >
              {{ item.hasNotionDatabaseId }}
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>
