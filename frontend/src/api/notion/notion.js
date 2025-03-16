import api from '@/api/index.js';
import { handleNotionDatabaseConnectionTest } from '@/api/apiResponse.js';

export const onDatabaseConnectTest = () => {
  return api
    .post(`/notion/connection-test`)
    .then((success) => handleNotionDatabaseConnectionTest(success))
    .catch((error) => handleNotionDatabaseConnectionTest(error));
};
