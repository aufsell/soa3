import { reactive, computed } from 'vue'
import { apiLogin, apiRegister } from '../api/auth'

const state = reactive({
  user: null,
  loading: false,
  error: null,
})

export function useAuth() {
  const isAuthed = computed(() => !!localStorage.getItem('access_token'))

  async function login({ email, password }) {
    state.loading = true
    state.error = null
    try {
      const res = await apiLogin({ email, password })
      localStorage.setItem('access_token', res.token)
    } catch (e) {
      state.error = e?.response?.data?.message || 'Ошибка входа'
      throw e
    } finally {
      state.loading = false
    }
  }

  async function register({ email, password, name }) {
    state.loading = true
    state.error = null
    try {
      const res = await apiRegister({ email, password, name })
      localStorage.setItem('access_token', res.token)
    } catch (e) {
      state.error = e?.response?.data?.message || 'Ошибка регистрации'
      throw e
    } finally {
      state.loading = false
    }
  }

  function logout() {
    localStorage.removeItem('access_token')
    state.user = null
  }

  return { state, isAuthed, login, register, logout }
}
