import axios from 'axios'

// const http = axios.create({
//   baseURL: 'https://localhost:8088/called/api/',
//   headers: { 'Content-Type': 'application/json' },
// })

const http = axios.create({
  baseURL: '/api',
  headers: { 'Content-Type': 'application/json' },
})

http.interceptors.request.use((config) => {
  const token = localStorage.getItem('access_token')

  // список путей, где токен не нужен
  const publicPaths = ['called/api/auth/login', 'called/api/auth/register']

  // если не публичный путь — добавить Bearer
  const isPublic = publicPaths.some((path) => config.url?.includes(path))
  if (!isPublic && token) {
    config.headers.Authorization = `Bearer ${token}`
  }

  return config
})

http.interceptors.response.use(
  (r) => r,
  (error) => {
    if (error?.response?.status === 401) {
      localStorage.removeItem('access_token')
      if (location.pathname !== '/auth') location.href = '/auth'
    }
    return Promise.reject(error)
  },
)

export default http
