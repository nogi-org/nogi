import { getUserGuidesApi } from '@/api/guide/guide.js';

export class UserGuideManager {
  async getUserGuides() {
    return await getUserGuidesApi();
  }
}
