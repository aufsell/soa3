import http from './http'

export async function apiLogin(payload) {
  // ожидаем, что backend вернёт { accessToken: "..." }
  const { data } = await http.post('called/api/auth/login', payload)
  return data
}

export async function apiRegister(payload) {
  const { data } = await http.post('called/api/auth/register', payload)
  return data
}
