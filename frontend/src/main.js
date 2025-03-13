import { createApp } from 'vue';
import { createPinia } from 'pinia';

import App from './App.vue';
import router, { setupRouter } from './router/router.js';
import './style/main.css';

// fontawesome
import { library } from '@fortawesome/fontawesome-svg-core';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { fas } from '@fortawesome/free-solid-svg-icons';
import { far } from '@fortawesome/free-regular-svg-icons';
// datepicker
import VueDatePicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css';

const app = createApp(App);

app.use(createPinia());
setupRouter();
app.use(router);
app.component('font-awesome-icon', FontAwesomeIcon).mount('#app');
app.component('VueDatePicker', VueDatePicker);
library.add(fas, far);
