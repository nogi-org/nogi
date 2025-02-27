import api from '@/api/index.js';
import { handleNotionDatabaseConnectionTest } from '@/api/apiResponse.js';

// todo: response 객체 생성필요
export const onDatabaseConnectTest = (payload) => {
  return api
    .post(`/notion/connection-test`, payload)
    .then((success) => handleNotionDatabaseConnectionTest(success))
    .catch((error) => handleNotionDatabaseConnectionTest(error));
};
