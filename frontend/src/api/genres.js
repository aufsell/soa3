import http from './http'

export function redistributeRewards(fromGenre, toGenre) {
  const url = `caller/redistribute-rewards/${encodeURIComponent(fromGenre)}/${encodeURIComponent(toGenre)}`
  return http.post(url).then((r) => r.data) // { transferredCount: number }
}
