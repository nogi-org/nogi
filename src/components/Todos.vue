<script setup lang="ts">
import '@/assets/main.css';
import { onMounted, ref } from 'vue';
import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

// Todo 타입 정의
interface Todo {
  id: string;
  content: string;
}

// todos 리스트 상태 관리
const todos = ref<Todo[]>([]);

// 전체 Todos 조회 (GET /demo/todos)
async function listTodos() {
  try {
    const response = await axios.get(`${API_BASE_URL}/demo/todos`);
    todos.value = response.data;
  } catch (error) {
    console.error("Error fetching todos:", error);
  }
}

// 새로운 Todo 생성 (POST /demo/todos)
async function createTodo() {
  const content = window.prompt("Todo content");
  if (!content) return;

  try {
    await axios.post(`${API_BASE_URL}/demo/todos`, { content });
    listTodos(); // 새로고침
  } catch (error) {
    console.error("Error creating todo:", error);
  }
}

// Todo 삭제 (DELETE /demo/todos/${id})
async function deleteTodo(id: string) {
  try {
    await axios.delete(`${API_BASE_URL}/demo/todos/${id}`);
    listTodos(); // 삭제 후 새로고침
  } catch (error) {
    console.error("Error deleting todo:", error);
  }
}

// 페이지가 로드될 때 전체 Todo 목록 가져오기
onMounted(() => {
  listTodos();
});
</script>

<template>
  <main>
    <div style="display:flex; justify-content: center;">
      <h1>Nogi</h1>
    </div>    
    <h2 class="coming-soon">🚀 곧 오픈 예정입니다! 🚀</h2>
    <button @click="createTodo">+ new</button>
    <ul v-if="todos.length > 0">
      <li v-for="todo in todos" :key="todo.id">
        {{ todo.content }}
        <button @click="deleteTodo(todo.id)" class="delete-btn">❌</button>
      </li>
    </ul>
  </main>
</template>

<style scoped>
ul {
  list-style: none;
  padding: 0;
}

li {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px;
  border-bottom: 1px solid #ddd;
}

.coming-soon {
  color: #ff5733;
  font-size: 1.5rem;
  font-weight: bold;
  text-align: center;
  margin-bottom: 20px;
}

.delete-btn {
  padding: 4px 8px;
  border: none;
  background-color: white;
  color: white;
  cursor: pointer;
  border-radius: 4px;
}

.delete-btn:hover {
  background-color: darkred;
}
</style>
