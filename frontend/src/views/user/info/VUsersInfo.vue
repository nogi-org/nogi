<script setup>
import CSettingTitle from '@/views/user/setting/components/CSettingTitle.vue';
import { AdminManager } from '@/manager/admin/AdminManager.js';
import { onMounted } from 'vue';
import router from '@/router/router.js';

const admin = new AdminManager();
onMounted(async () => {
  await admin.getUsersInfo();
});

const goToUserNotionPages = () => {
  router.push({ name: 'userNotionPages' });
};
</script>

<template>
  <div>
    <CSettingTitle title="Action" />
    <div class="border border-main p-4 mb-10">
      <button
        class="bg-action px-3 py-1 rounded-md"
        @click="admin.updateNotionPageIdOfAllUser()"
      >
        모든 유저 Notion Page ID 업데이트
      </button>
    </div>

    <CSettingTitle :title="`User (${admin.usersInfo.value.length})`" />
    <div class="relative overflow-x-auto rounded-md">
      <table class="w-full text-sm text-left text-neutral">
        <thead class="text-xs uppercase bg-main font-noto_sans_m">
          <tr>
            <th scope="col" class="px-6 py-3">Git ID</th>
            <th scope="col" class="px-6 py-3">Role</th>
            <th scope="col" class="px-6 py-3">Owner</th>
            <th scope="col" class="px-6 py-3">Email</th>
            <th scope="col" class="px-6 py-3">Repository</th>
            <th scope="col" class="px-6 py-3">Branch</th>
            <th scope="col" class="px-6 py-3">Page ID</th>
            <th scope="col" class="px-6 py-3">Git Token</th>
            <th scope="col" class="px-6 py-3">Notion Token</th>
            <th scope="col" class="px-6 py-3">Notion Database</th>
          </tr>
        </thead>
        <tbody>
          <tr
            class=""
            v-for="(item, index) in admin.usersInfo.value"
            :key="index"
            @click="goToUserNotionPages"
          >
            <th scope="row" class="px-6 py-4">{{ item.githubId }}</th>
            <th scope="row" class="px-6 py-4">{{ item.role }}</th>
            <td class="px-6 py-4">{{ item.owner }}</td>
            <td class="px-6 py-4 whitespace-nowrap">{{ item.email }}</td>
            <td class="px-6 py-4 whitespace-nowrap">{{ item.repository }}</td>
            <td class="px-6 py-4">{{ item.branch }}</td>
            <td class="px-6 py-4 whitespace-nowrap">{{ item.pageId }}</td>
            <td
              class="px-6 py-4"
              :class="item.hasGithubToken ? 'text-action' : 'text-danger'"
            >
              {{ item.hasGithubToken }}
            </td>
            <td
              class="px-6 py-4"
              :class="item.hasNotionToken ? 'text-action' : 'text-danger'"
            >
              {{ item.hasNotionToken }}
            </td>
            <td
              class="px-6 py-4"
              :class="item.hasNotionDatabaseId ? 'text-action' : 'text-danger'"
            >
              {{ item.hasNotionDatabaseId }}
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>
