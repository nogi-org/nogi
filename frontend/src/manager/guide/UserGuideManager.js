import { getUserGuidesApi } from '@/api/guide/usageGuide.js';

export class UserGuideManager {
  async getUserGuides() {
    return await getUserGuidesApi();
  }
}
