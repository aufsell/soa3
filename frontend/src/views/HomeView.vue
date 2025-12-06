<template>
  <div class="page">
    <header class="topbar">
      <h2>Фильмы</h2>
      <div class="right">
        <label class="size">
          На странице:
          <select v-model.number="size" @change="onSizeChange">
            <option :value="10">10</option>
            <option :value="20">20</option>
            <option :value="50">50</option>
          </select>
        </label>
      </div>
    </header>

    <!-- 🟦 Перераспределение наград -->
    <div class="card transfer-card">
      <div class="transfer-row">
        <div class="transfer-title">Перераспределить «Оскары» между жанрами</div>
        <div class="transfer-controls">
          <label class="inline">
            Из жанра:
            <select class="input select" v-model="fromGenre">
              <option disabled value="">Выберите жанр</option>
              <option v-for="g in GENRES" :key="g" :value="g">{{ g }}</option>
            </select>
          </label>
          <label class="inline">
            В жанр:
            <select class="input select" v-model="toGenre">
              <option disabled value="">Выберите жанр</option>
              <option v-for="g in GENRES" :key="g" :value="g">{{ g }}</option>
            </select>
          </label>

          <button class="btn" :disabled="!canTransfer || transferLoading" @click="onTransfer">
            {{ transferLoading ? 'Выполняю…' : 'Перераспределить' }}
          </button>
        </div>
      </div>

      <p v-if="transferResult" class="success">
        Перераспределено «Оскаров»: <b>{{ transferResult }}</b>
      </p>
      <p v-if="transferError" class="error">{{ transferError }}</p>
    </div>

    <!-- ФИЛЬТРЫ -->
    <div class="filters card">
      <div class="filters-header">
        <strong>Фильтры</strong>
        <div class="filters-actions">
          <button class="link" @click="addFilter">+ условие</button>
          <button class="link danger" @click="clearFilters" :disabled="filters.length === 0">
            Очистить
          </button>
          <button class="btn" @click="applyFilters">Применить</button>
        </div>
      </div>

      <div class="filter-rows">
        <div class="filter-row" v-for="(f, idx) in filters" :key="f.id">
          <select class="input select" v-model="f.field" @change="onFieldChange(f)">
            <option v-for="fld in FIELDS" :key="fld.key" :value="fld.key">{{ fld.label }}</option>
          </select>

          <select class="input select" v-model="f.op">
            <option v-for="op in opsFor(f.field)" :key="op" :value="op">{{ OP_LABELS[op] }}</option>
          </select>

          <template v-if="inputKind(f) === 'enum-multi' && f.op === 'in'">
            <select class="input select" v-model="f.values" multiple>
              <option v-for="opt in enumOptionsFor(f.field)" :key="opt" :value="opt">
                {{ opt }}
              </option>
            </select>
          </template>

          <template v-else-if="inputKind(f) === 'enum'">
            <select class="input select" v-model="f.value">
              <option value="" disabled>Выберите…</option>
              <option v-for="opt in enumOptionsFor(f.field)" :key="opt" :value="opt">
                {{ opt }}
              </option>
            </select>
          </template>

          <template v-else-if="inputKind(f) === 'date'">
            <input class="input" type="date" v-model="f.value" />
          </template>

          <template v-else>
            <input
              class="input"
              :type="isNumberField(f.field) ? 'number' : 'text'"
              v-model="f.value"
              :placeholder="f.op === 'in' ? 'Через запятую: a,b,c' : 'Значение'"
            />
          </template>

          <button class="link danger" @click="removeFilter(idx)">Удалить</button>
        </div>
        <p class="hint">Поддерживаются операторы: =, ≠, &gt;, ≥, &lt;, ≤, IN.</p>
      </div>
    </div>
    <!-- КНОПКА СОЗДАНИЯ -->
    <div class="card" style="margin-bottom: 12px; padding: 12px">
      <button class="btn" @click="openCreate">+ Добавить фильм</button>
    </div>

    <!-- ТАБЛИЦА -->
    <div class="card">
      <table class="table">
        <thead>
          <tr>
            <th
              v-for="col in COLUMNS"
              :key="col.key"
              @click="toggleSort(col.key)"
              :class="thClass(col.key)"
            >
              {{ col.title }}
            </th>
            <th class="actions-col">Действия</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="m in rows" :key="m.id">
            <td v-for="col in COLUMNS" :key="col.key">
              {{ formatValue(getVal(m, col.key)) }}
            </td>

            <td class="actions">
              <button
                class="edit-btn"
                :disabled="!!editing"
                @click="openEdit(m)"
                title="Редактировать"
              >
                ✏️
              </button>
              <button
                class="trash-btn"
                :disabled="deletingId === m.id"
                @click="onDelete(m)"
                title="Удалить фильм"
              >
                🗑
              </button>
            </td>
          </tr>
          <tr v-if="!loading && rows.length === 0">
            <td :colspan="COLUMNS.length" class="empty">Ничего не найдено</td>
          </tr>
        </tbody>
      </table>

      <div v-if="loading" class="loading">Загрузка…</div>
      <div v-if="error" class="error">{{ error }}</div>
    </div>

    <!-- ПАГИНАЦИЯ -->
    <div class="pager" v-if="totalPages > 1">
      <button class="pg-btn" :disabled="page === 0" @click="goTo(page - 1)">‹</button>
      <span class="pg-info">Стр. {{ page + 1 }} из {{ totalPages }}</span>
      <button class="pg-btn" :disabled="page >= totalPages - 1" @click="goTo(page + 1)">›</button>
    </div>

    <!-- МОДАЛЬНОЕ ОКНО РЕДАКТИРОВАНИЯ -->
    <div v-if="editing" class="modal-backdrop" @click.self="cancelEdit">
      <div class="modal">
        <h3 v-if="!isCreateMode">Редактирование фильма #{{ editId }}</h3>
        <h3 v-else>Создание фильма</h3>

        <div class="form">
          <label>
            Название
            <input
              v-model="editForm.name"
              type="text"
              :class="{
                invalid: editForm.name == null || !editForm.name.trim(),
              }"
            />
          </label>

          <div class="two">
            <label>
              Координата X
              <input
                v-model.number="editForm.coordinates.x"
                :class="{
                  invalid:
                    !Number.isInteger(editForm.coordinates.x) ||
                    normalizeNumber(editForm.coordinates.x) === false,
                }"
              />
            </label>
            <label>
              Координата Y
              <input
                v-model.number="editForm.coordinates.y"
                type="number"
                step="0.01"
                :class="{
                  invalid: normalizeNumber(editForm.coordinates.y) === false,
                }"
              />
            </label>
          </div>

          <div class="two">
            <label>
              Оскаров
              <input
                v-model.number="editForm.oscarsCount"
                type="number"
                min="1"
                step="1"
                :class="{
                  invalid: !Number.isInteger(editForm.oscarsCount) || editForm.oscarsCount <= 0,
                }"
              />
            </label>
            <label>
              Жанр
              <select v-model="editForm.genre">
                <option v-for="g in GENRES" :key="g" :value="g">{{ g }}</option>
              </select>
            </label>
          </div>

          <label>
            MPAA рейтинг
            <select v-model="editForm.mpaaRating">
              <option v-for="m in MPAA" :key="m" :value="m">{{ m }}</option>
            </select>
          </label>

          <fieldset class="fieldset">
            <legend>Оператор</legend>
            <label>
              Имя
              <input
                v-model="editForm.operator.name"
                type="text"
                :class="{
                  invalid: editForm.operator.name == null || !editForm.operator.name.trim(),
                }"
              />
            </label>
            <div class="two">
              <label>
                Рост
                <input
                  v-model.number="editForm.operator.height"
                  type="number"
                  min="1"
                  step="1"
                  :class="{
                    invalid:
                      !Number.isInteger(editForm.operator.height) || editForm.operator.height <= 0,
                  }"
                />
              </label>
              <label>
                Вес
                <input
                  v-model.number="editForm.operator.weight"
                  type="number"
                  min="1"
                  step="1"
                  :class="{
                    invalid:
                      !Number.isInteger(editForm.operator.weight) || editForm.operator.weight <= 0,
                  }"
                />
              </label>
            </div>
            <div class="three">
              <label>
                Локация X
                <input
                  v-model.number="editForm.operator.location.x"
                  type="number"
                  :class="{
                    invalid:
                      normalizeNumber(editForm.operator.location.x) === false ||
                      !Number.isInteger(editForm.operator.location.x),
                  }"
                />
              </label>
              <label>
                Локация Y
                <input
                  v-model.number="editForm.operator.location.y"
                  type="number"
                  :class="{
                    invalid:
                      normalizeNumber(editForm.operator.location.y) === false ||
                      !Number.isInteger(editForm.operator.location.y),
                  }"
                />
              </label>
              <label>
                Локация Z
                <input
                  v-model.number="editForm.operator.location.z"
                  type="number"
                  :class="{
                    invalid:
                      normalizeNumber(editForm.operator.location.z) === false ||
                      !Number.isInteger(editForm.operator.location.z),
                  }"
                />
              </label>
            </div>
          </fieldset>
        </div>

        <div class="modal-actions">
          <button class="btn" :disabled="saving || !isFormValid" @click="saveEdit">
            {{
              saving
                ? isCreateMode
                  ? 'Создаю…'
                  : 'Сохраняю…'
                : isCreateMode
                  ? 'Создать'
                  : 'Сохранить'
            }}
          </button>
          <button class="link" :disabled="saving" @click="cancelEdit">Отмена</button>
        </div>

        <p class="error" v-if="editError">{{ editError }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { searchMovies, updateMovie, deleteMovie, createMovie } from '../api/movies'
import { redistributeRewards } from '../api/genres'

// ---- Табличные колонки (все поля, в т.ч. вложенные)
const COLUMNS = [
  { title: 'ID', key: 'id' },
  { title: 'Название', key: 'name' },
  { title: 'Дата', key: 'creationDate' },
  { title: 'Оскары', key: 'oscarsCount' },
  { title: 'Жанр', key: 'genre' },
  { title: 'MPAA', key: 'mpaaRating' },
  // { title: 'Coord.ID', key: 'coordinates.id' },
  { title: 'Coord.X', key: 'coordinates.x' },
  { title: 'Coord.Y', key: 'coordinates.y' },
  // { title: 'Oper.ID', key: 'operator.id' },
  { title: 'Oper.Name', key: 'operator.name' },
  { title: 'Oper.Height', key: 'operator.height' },
  { title: 'Oper.Weight', key: 'operator.weight' },
  // { title: 'Loc.ID', key: 'operator.location.id' },
  { title: 'Loc.X', key: 'operator.location.x' },
  { title: 'Loc.Y', key: 'operator.location.y' },
  { title: 'Loc.Z', key: 'operator.location.z' },
]

// ---- Справочники
const GENRES = ['DRAMA', 'FANTASY', 'THRILLER']
const MPAA = ['PG', 'R', 'NC_17']

// ---- Фильтрация
const FIELDS = [
  { key: 'id', label: 'ID', type: 'number' },
  { key: 'name', label: 'Название', type: 'string' },
  { key: 'coordinates.x', label: 'Coordinates X', type: 'number' },
  { key: 'coordinates.y', label: 'Coordinates Y', type: 'number' },
  { key: 'creationDate', label: 'Дата создания', type: 'date' },
  { key: 'oscarsCount', label: 'Оскары', type: 'number' },
  { key: 'genre', label: 'Жанр', type: 'enum', options: GENRES },
  { key: 'mpaaRating', label: 'MPAA', type: 'enum', options: MPAA },
  { key: 'operator.name', label: 'Оператор — имя', type: 'string' },
  { key: 'operator.height', label: 'Оператор — рост', type: 'number' },
  { key: 'operator.weight', label: 'Оператор — вес', type: 'number' },
  { key: 'operator.location.x', label: 'Локация X', type: 'number' },
  { key: 'operator.location.y', label: 'Локация Y', type: 'number' },
  { key: 'operator.location.z', label: 'Локация Z', type: 'number' },
]

const OP_LABELS = { eq: 'равно', ne: 'не равно', gt: '>', gte: '≥', lt: '<', lte: '≤', in: 'IN' }
const OPS_BY_TYPE = {
  string: ['eq', 'ne', 'in'],
  enum: ['eq', 'ne', 'in'],
  number: ['eq', 'ne', 'gt', 'gte', 'lt', 'lte', 'in'],
  date: ['eq', 'ne', 'gt', 'gte', 'lt', 'lte', 'in'],
}

// ---- Таблица: состояние
const page = ref(0)
const size = ref(20)
const sort = ref('id:desc')
const loading = ref(false)
const error = ref('')
const rows = ref([])
const totalElements = ref(0)
const totalPages = ref(1)

const sortKey = computed(() => sort.value.split(':')[0])
const sortDir = computed(() => sort.value.split(':')[1] || 'asc')
const isCreateMode = computed(() => !editId.value)
function openCreate() {
  editId.value = null
  editForm.value = makeEmptyForm()
  editError.value = ''
  editing.value = true
}

const isFormValid = computed(() => {
  const f = editForm.value
  if (!f) return false
  // имя не пустое
  if (!f.name || !f.name.trim()) return false
  // координаты (могут быть дробные, но не null)
  if (f.coordinates.x == null || f.coordinates.y == null) return false
  // только > 0 и целое
  if (!Number.isInteger(f.oscarsCount) || f.oscarsCount <= 0) return false
  if (normalizeNumber(f.coordinates.x) === false) return false
  if (normalizeNumber(f.coordinates.y) === false) return false
  if (normalizeNumber(f.operator.location.z) === false) return false
  if (normalizeNumber(f.operator.location.y) === false) return false
  if (normalizeNumber(f.operator.location.x) === false) return false
  // жанр и рейтинг — обязательные
  if (!f.genre || !f.mpaaRating) return false
  // оператор (допускается null, но если есть — проверяем)
  const op = f.operator
  if (op) {
    if (!op.name || !op.name.trim()) return false
    if (!Number.isInteger(op.height) || op.height <= 0) return false
    if (!Number.isInteger(op.weight) || op.weight <= 0) return false
    if (op.location) {
      const loc = op.location
      if (loc.x == null || loc.y == null || loc.z == null) return false
    } else return false
  }
  return true
})

function makeEmptyForm() {
  return {
    name: '',
    id: 999,
    coordinates: { x: 0, y: 0 },
    oscarsCount: 0,
    genre: GENRES[0],
    mpaaRating: MPAA[0],
    operator: {
      name: '',
      height: 0,
      weight: 0,
      location: { x: 0, y: 0, z: 0 },
    },
  }
}
const deletingId = ref(null)
const editing = ref(false)
const saving = ref(false)
const editError = ref('')
const editId = ref(null)
const editForm = ref(makeEmptyForm())
function deepClone(o) {
  return JSON.parse(JSON.stringify(o))
}

function openEdit(movie) {
  editId.value = movie.id
  editForm.value = deepClone({
    name: movie.name ?? '',
    coordinates: {
      x: Number(getVal(movie, 'coordinates.x') ?? 0),
      y: Number(getVal(movie, 'coordinates.y') ?? 0),
    },
    oscarsCount: Number(movie.oscarsCount ?? 0),
    genre: movie.genre ?? GENRES[0],
    mpaaRating: movie.mpaaRating ?? MPAA[0],
    operator: {
      name: getVal(movie, 'operator.name') ?? '',
      height: Number(getVal(movie, 'operator.height') ?? 0),
      weight: Number(getVal(movie, 'operator.weight') ?? 0),
      location: {
        x: Number(getVal(movie, 'operator.location.x') ?? 0),
        y: Number(getVal(movie, 'operator.location.y') ?? 0),
        z: Number(getVal(movie, 'operator.location.z') ?? 0),
      },
    },
  })
  editError.value = ''
  editing.value = true
}

function cancelEdit() {
  if (saving.value) return
  editing.value = false
  editId.value = null
  editForm.value = makeEmptyForm()
  editError.value = ''
}
function normalizeNumber(value) {
  if (value === null || value === undefined || value === '') return false

  // Приводим к строке
  const str = String(value).trim().replace(',', '.')
  const num = Number(str)
  if (isNaN(num)) return false

  // Проверяем количество знаков после запятой/точки
  const parts = str.split('.')
  if (parts.length === 1) return true // нет дробной части
  return parts[1].length <= 5
}

async function saveEdit() {
  saving.value = true
  editError.value = ''
  try {
    const payload = deepClone(editForm.value)
    if (isCreateMode.value) {
      console.log('fff' + JSON.stringify(payload))
      // если id пустой — не отправляем его вовсе
      await createMovie(payload) // POST /movies
    } else {
      await updateMovie(editId.value, payload) // PUT /movies/{id}
    }
    await load() // обновляем таблицу
    editing.value = false
    editId.value = null
    editForm.value = makeEmptyForm()
    editError.value = ''
  } catch (e) {
    editError.value = e?.response?.data?.message || 'Не удалось сохранить изменения'
  } finally {
    saving.value = false
  }
}

function thClass(key) {
  return {
    sortable: true,
    active: sortKey.value === key,
    asc: sortKey.value === key && sortDir.value === 'asc',
    desc: sortKey.value === key && sortDir.value === 'desc',
  }
}
function toggleSort(key) {
  const same = sortKey.value === key
  sort.value = same ? `${key}:${sortDir.value === 'asc' ? 'desc' : 'asc'}` : `${key}:asc`
  page.value = 0
  load()
}
function onSizeChange() {
  page.value = 0
  load()
}
function goTo(p) {
  page.value = Math.max(0, p)
  load()
}

// ---- Фильтры-конструктор
let nextId = 1
const filters = ref([])
function addFilter() {
  const fld = FIELDS[0]
  filters.value.push({
    id: nextId++,
    field: fld.key,
    op: OPS_BY_TYPE[fld.type][0],
    value: '',
    values: [],
  })
}
function removeFilter(idx) {
  filters.value.splice(idx, 1)
}
function clearFilters() {
  filters.value = []
  page.value = 0
  load()
}
function applyFilters() {
  page.value = 0
  load()
}
function fieldMeta(fieldKey) {
  return FIELDS.find((f) => f.key === fieldKey) || { type: 'string' }
}
function opsFor(fieldKey) {
  return OPS_BY_TYPE[fieldMeta(fieldKey).type] || ['eq', 'ne']
}
function enumOptionsFor(fieldKey) {
  return fieldMeta(fieldKey).options || []
}
function isNumberField(fieldKey) {
  return fieldMeta(fieldKey).type === 'number'
}

function inputKind(f) {
  const meta = fieldMeta(f.field)
  if (meta.type === 'enum') return f.op === 'in' ? 'enum-multi' : 'enum'
  return meta.type
}
function onFieldChange(f) {
  const allowed = opsFor(f.field)
  if (!allowed.includes(f.op)) f.op = allowed[0]
  f.value = ''
  f.values = []
}

// ---- helpers
function getVal(obj, path) {
  return path.split('.').reduce((acc, k) => (acc != null ? acc[k] : undefined), obj)
}
function formatValue(v) {
  if (v === null || v === undefined) return '—'
  return v
}
function parseCsv(val, fieldKey) {
  const parts = String(val)
    .split(',')
    .map((s) => s.trim())
    .filter(Boolean)
  const meta = fieldMeta(fieldKey)
  return meta.type === 'number' ? parts.map(Number) : parts
}

function buildFiltersObject() {
  const out = {}
  for (const f of filters.value) {
    const key = `${f.field}[${f.op}]`
    const meta = fieldMeta(f.field)
    let val
    if (f.op === 'in') {
      val =
        meta.type === 'enum'
          ? Array.isArray(f.values)
            ? f.values
            : []
          : Array.isArray(f.value)
            ? f.value
            : parseCsv(f.value, f.field)
    } else {
      val =
        meta.type === 'number'
          ? f.value === '' || f.value === null
            ? null
            : Number(f.value)
          : f.value
    }
    const isEmpty =
      (f.op === 'in' && Array.isArray(val) && val.length === 0) ||
      (f.op !== 'in' && (val === '' || val == null))
    if (!isEmpty) out[key] = val
  }
  return out
}

function buildBody() {
  return {
    filters: buildFiltersObject(),
    sort: [sort.value],
    page: page.value,
    size: size.value,
  }
}

async function load() {
  loading.value = true
  error.value = ''
  try {
    const data = await searchMovies(buildBody())
    rows.value = data.content ?? []
    totalElements.value = data.totalElements ?? 0
    totalPages.value = data.totalPages ?? 1
  } catch (e) {
    error.value = e?.response?.data?.message || 'Не удалось загрузить фильмы'
    rows.value = []
    totalElements.value = 0
    totalPages.value = 1
  } finally {
    loading.value = false
  }
}

// 🟦 Перераспределение: состояние и логика
const fromGenre = ref('')
const toGenre = ref('')
const transferLoading = ref(false)
const transferResult = ref(0)
const transferError = ref('')

const canTransfer = computed(
  () => !!fromGenre.value && !!toGenre.value && fromGenre.value !== toGenre.value,
)

async function onTransfer() {
  if (!canTransfer.value) return
  transferError.value = ''
  transferResult.value = 0
  transferLoading.value = true
  try {
    const res = await redistributeRewards(fromGenre.value, toGenre.value)
    // a) показать результат
    transferResult.value = res?.transferredCount ?? 0
    // б) обновить таблицу, НЕ сбрасывая фильтры/сортировку/страницу
    await load()
  } catch (e) {
    transferError.value = e?.response?.data?.message || 'Не удалось перераспределить награды'
  } finally {
    transferLoading.value = false
  }
}

async function onDelete(movie) {
  if (deletingId.value) return
  deletingId.value = movie.id
  try {
    await deleteMovie(movie.id)
    await load()
  } catch (e) {
    console.error(e)
    alert('Не удалось удалить фильм')
  } finally {
    if (deletingId.value === movie.id) deletingId.value = null
  }
}

onMounted(load)
</script>

<style scoped>
.page {
  padding: 24px;
  font-family:
    system-ui,
    -apple-system,
    Segoe UI,
    Roboto,
    Arial,
    sans-serif;
  color: #0b3ba7;
}

.topbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12px;
}
.right {
  display: flex;
  gap: 12px;
  align-items: center;
}
.size select {
  margin-left: 6px;
}

.card {
  background: #fff;
  border: 1px solid #e6edff;
  border-radius: 14px;
  box-shadow: 0 8px 24px rgba(20, 60, 200, 0.05);
}
.transfer-card {
  padding: 12px;
  margin-bottom: 12px;
}
.transfer-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  flex-wrap: wrap;
}
.transfer-title {
  font-weight: 700;
}
.transfer-controls {
  display: flex;
  gap: 10px;
  align-items: center;
}
.inline {
  display: flex;
  gap: 6px;
  align-items: center;
}

.filters {
  padding: 12px;
  margin-bottom: 12px;
}
.filters-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 8px;
}
.filters-actions {
  display: flex;
  gap: 8px;
  align-items: center;
}

.filter-rows {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.filter-row {
  display: grid;
  grid-template-columns: 1.2fr 0.9fr 2fr auto;
  gap: 8px;
  align-items: center;
}
.hint {
  margin: 8px 0 0;
  color: #5a77c7;
  font-size: 13px;
}

.input,
.select {
  padding: 8px 10px;
  border: 1px solid #cfe0ff;
  border-radius: 8px;
  outline: none;
  background: #fff;
  color: #0b3ba7;
}
.input:focus,
.select:focus {
  border-color: #2f5eea;
}

.btn {
  padding: 8px 12px;
  background: #2f5eea;
  color: #fff;
  border: 0;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
}
.link {
  border: 0;
  background: transparent;
  color: #1f56ff;
  text-decoration: underline;
  cursor: pointer;
  padding: 0;
}
.link.danger {
  color: #c62828;
  text-decoration: none;
}

.table {
  width: 100%;
  border-collapse: collapse;
  min-width: 1100px;
}
.table th,
.table td {
  padding: 10px 12px;
  border-bottom: 1px solid #eef2ff;
  white-space: nowrap;
}
.table thead th {
  background: #f5f8ff;
  color: #0b3ba7;
  user-select: none;
  cursor: pointer;
}
.sortable.active.asc::after {
  content: ' ▲';
  color: #2f5eea;
}
.sortable.active.desc::after {
  content: ' ▼';
  color: #2f5eea;
}

.loading {
  padding: 12px;
  color: #1b3f9e;
}
.error {
  padding: 12px;
  color: #c62828;
}
.success {
  padding: 8px 0 0;
  color: #087f23;
}

.empty {
  text-align: center;
  color: #5a77c7;
}
.pager {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 12px;
  color: #0b3ba7;
}
.pg-btn {
  border: 1px solid #cfe0ff;
  background: #fff;
  color: #1f56ff;
  border-radius: 8px;
  padding: 6px 10px;
  cursor: pointer;
}
.pg-btn[disabled] {
  opacity: 0.5;
  cursor: default;
}
.pg-info {
  color: #0b3ba7;
}
.actions-col {
  width: 110px;
  text-align: right;
}
.actions {
  text-align: right;
}
.trash-btn {
  border: 0;
  background: #ffeded;
  color: #c01818;
  padding: 6px 10px;
  border-radius: 8px;
  cursor: pointer;
}
.trash-btn[disabled] {
  opacity: 0.5;
  cursor: default;
}
.trash-btn:hover:not([disabled]) {
  background: #ffdcdc;
}

/* Кнопка редактирования рядом с корзиной */
.edit-btn {
  border: 0;
  background: #eef6ff;
  color: #0b3ba7;
  padding: 6px 10px;
  border-radius: 8px;
  cursor: pointer;
  margin-right: 6px;
}
.edit-btn[disabled] {
  opacity: 0.5;
  cursor: default;
}
.edit-btn:hover:not([disabled]) {
  background: #dcebff;
}

/* Модалка */
.modal-backdrop {
  position: fixed;
  inset: 0;
  background: rgba(17, 27, 53, 0.45);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px;
  z-index: 50;
}
.invalid {
  border-color: #d32f2f;
  background: #fff5f5;
}
.btn:disabled {
  background: #cfd6e6; /* светло-серая заливка */
  color: #7a8194; /* серый текст */
  cursor: not-allowed;
  border: 0;
  opacity: 1; /* чтобы не бледнело ещё сильнее */
}
.modal {
  background: #fff;
  border-radius: 12px;
  width: 720px;
  max-width: 100%;
  padding: 16px;
  box-shadow: 0 8px 24px rgba(10, 20, 60, 0.2);
}
.form {
  display: grid;
  gap: 10px;
  margin-top: 12px;
}
.two {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px;
}
.three {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  gap: 10px;
}
.form label {
  display: grid;
  gap: 6px;
}
.form input,
.form select {
  height: 36px;
  padding: 6px 10px;
  border: 1px solid #dbe6ff;
  border-radius: 8px;
}
.fieldset {
  border: 1px dashed #dbe6ff;
  padding: 12px;
  border-radius: 8px;
}
.fieldset legend {
  padding: 0 6px;
  color: #0b3ba7;
}
.modal-actions {
  display: flex;
  gap: 8px;
  justify-content: flex-end;
  margin-top: 12px;
}
</style>
