import { getUserGuidesApi } from '@/api/guide/guide.js';

export class GuideManager {
  async getUserGuides() {
    return await getUserGuidesApi();
  }
}
