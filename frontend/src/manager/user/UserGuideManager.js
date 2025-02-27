import { getUserGuidesApi } from '@/api/user/userGuide.js';

export class UserGuideManager {
  async getUserGuides() {
    return await getUserGuidesApi();
  }
}
