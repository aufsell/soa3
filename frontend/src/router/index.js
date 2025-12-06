import { createRouter, createWebHistory } from 'vue-router';
import AuthView from '../views/AuthView.vue';
import HomeView from '../views/HomeView.vue';

const routes = [
  { path: '/auth', name: 'auth', component: AuthView, meta: { public: true } },
  { path: '/', name: 'home', component: HomeView }, // защищённый
  { path: '/:pathMatch(.*)*', redirect: '/' }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

// Guard
router.beforeEach((to) => {
  const isPublic = to.meta?.public;
  const token = localStorage.getItem('access_token');
  if (!isPublic && !token) return { name: 'auth' };
});

export default router;
