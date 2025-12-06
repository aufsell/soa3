import http from './http'
export function searchMovies(body) {
  return http.post('called/api/movies/search', body).then((r) => r.data)
}

export function deleteMovie(id) {
  return http.delete(`called/api/movies/${id}`).then((r) => r.data)
}

export function updateMovie(id, body) {
  return http.put(`called/api/movies/${id}`, body).then((r) => r.data)
}
export function createMovie(body) {
  return http.post('called/api/movies', body).then((r) => r.data)
}
