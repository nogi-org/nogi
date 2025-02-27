import { useSpinnerStore } from '@/stores/spinnerStore.js';
import { useNotifyStore } from '@/stores/notifyStore.js';
import { onDatabaseConnectTest } from '@/api/notion/notion.js';

export class NotionManager {
  #notifyStore = useNotifyStore();
  #spinnerStore = useSpinnerStore();

  async onDatabaseConnectionTest(botToken, databaseId) {
    if (botToken.trim() === '' || databaseId.trim() === '') {
      this.#notifyStore.fail({
        message: `Notion Bot Token과 Notion Database ID를 확인해주세요.`
      });
      return;
    }

    const parsedDatabaseId = this.#parseDatabaseId(databaseId);
    if (!parsedDatabaseId) {
      this.#notifyStore.fail({
        message: `Notion DatabaseID를 확인해주세요.\n"https://www.notion.so/{databaseId}?v=xxx&pvs=x" 또는 "https://www.notion.so/{your-domain}/{databaseId}?v=xxx&pvs=x"\n형식으로 입력해주세요.`
      });
      return;
    }

    this.#spinnerStore.on();
    const response = await onDatabaseConnectTest({
      notionBotToken: botToken,
      notionDatabaseId: parsedDatabaseId
    });
    this.#spinnerStore.off();
    this.#notifyStore.onActive(response);
    return {
      isSuccess: response.isSuccess,
      botToken: botToken,
      databaseId: parsedDatabaseId
    };
  }

  #parseDatabaseId(databaseId) {
    if (typeof databaseId !== 'string' || !databaseId.includes('?')) {
      return false;
    }
    const [path] = databaseId.split('?');
    const trimmedPath = path.trim();
    if (!trimmedPath) {
      return false;
    }
    const lastSegment = trimmedPath.split('/').pop();
    return lastSegment || false;
  }
}
