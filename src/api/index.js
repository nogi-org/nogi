import axios from 'axios';
import { setInterceptors } from '@/api/interceptors.js';

const instance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL
});
const api = setInterceptors(instance);
export default api;
