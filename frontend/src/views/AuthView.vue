<template>
  <div class="auth-wrapper">
    <div class="card">
      <h1 class="title">Welcome</h1>
      <p class="subtitle">{{ mode === 'login' ? 'Войдите в аккаунт' : 'Создайте аккаунт' }}</p>

      <form @submit.prevent="onSubmit">
        <!-- name появляется только при регистрации -->
        <template v-if="mode === 'register'">
          <label class="label">Имя</label>
          <input v-model.trim="name" type="text" required class="input" placeholder="Введите имя" />
        </template>

        <label class="label">Email</label>
        <input
          v-model.trim="email"
          type="email"
          required
          class="input"
          placeholder="you@example.com"
        />

        <label class="label">Пароль</label>
        <input
          v-model.trim="password"
          type="password"
          required
          class="input"
          placeholder="••••••••"
        />

        <button class="btn" :disabled="auth.state.loading">
          {{ mode === 'login' ? 'Войти' : 'Зарегистрироваться' }}
        </button>
      </form>

      <p v-if="auth.state.error" class="error">{{ auth.state.error }}</p>

      <div class="switch">
        <span>{{ mode === 'login' ? 'Нет аккаунта?' : 'Уже есть аккаунт?' }}</span>
        <button class="link" @click="toggleMode">
          {{ mode === 'login' ? 'Регистрация' : 'Войти' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuth } from '../composables/useAuth'

const auth = useAuth()
const router = useRouter()

const mode = ref('login')
const name = ref('')
const email = ref('')
const password = ref('')

function toggleMode() {
  auth.state.error = null
  mode.value = mode.value === 'login' ? 'register' : 'login'
}

async function onSubmit() {
  try {
    if (mode.value === 'login') {
      await auth.login({ email: email.value, password: password.value })
    } else {
      await auth.register({
        name: name.value,
        email: email.value,
        password: password.value,
      })
    }
    router.push({ name: 'home' })
  } catch (_) {}
}
</script>

<style scoped>
.auth-wrapper {
  min-height: 100vh;
  display: grid;
  place-items: center;
  background: #f5f8ff;
}
.card {
  width: 100%;
  max-width: 380px;
  background: #fff;
  border: 1px solid #e6edff;
  border-radius: 16px;
  padding: 28px;
  box-shadow: 0 8px 24px rgba(20, 60, 200, 0.08);
}
.title {
  margin: 0 0 4px;
  font-size: 26px;
  color: #0b3ba7;
  font-weight: 700;
}
.subtitle {
  margin: 0 0 16px;
  color: #5a77c7;
}
.label {
  display: block;
  margin: 12px 0 6px;
  color: #1b3f9e;
  font-size: 14px;
}
.input {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #cfe0ff;
  border-radius: 10px;
  outline: none;
  transition: border 0.15s ease;
}
.input:focus {
  border-color: #2f5eea;
}
.btn {
  margin-top: 18px;
  width: 100%;
  padding: 10px 14px;
  border: none;
  border-radius: 10px;
  background: #2f5eea;
  color: white;
  font-weight: 600;
  cursor: pointer;
}
.btn[disabled] {
  opacity: 0.7;
  cursor: default;
}
.switch {
  margin-top: 14px;
  font-size: 14px;
  color: #3f5ecb;
  display: flex;
  gap: 6px;
  align-items: center;
  justify-content: center;
}
.link {
  background: transparent;
  border: none;
  color: #1f56ff;
  text-decoration: underline;
  cursor: pointer;
  padding: 0;
}
.error {
  margin-top: 10px;
  color: #c62828;
  font-size: 14px;
}
</style>
