# Архитектура проекта после миграции на SOAP

## Диаграмма компонентов

```
┌─────────────────┐
│   Frontend      │
│   (Vue.js)      │
└────────┬────────┘
         │ HTTPS
         ↓
┌─────────────────────────────┐
│   API Gateway               │
│   (Spring Cloud Gateway)    │
│   Port: 8443 (HTTPS)        │
└────┬────────────────┬───────┘
     │                │
     │ /caller/**     │ /called/**
     ↓                ↓
┌──────────────┐  ┌──────────────────┐
│ caller-      │  │ called-adapter   │
│ service      │  │ (Spring Boot)    │
│ (Spring      │  │ Port: 8090       │
│  Boot)       │  │                  │
└──────────────┘  └────────┬─────────┘
                           │ SOAP
                           ↓
                  ┌──────────────────────┐
                  │ called-service       │
                  │ (Payara + EJB)       │
                  │ Port: 8080           │
                  │                      │
                  │ SOAP Services:       │
                  │ - MovieSoapService   │
                  │ - AuthSoapService    │
                  │                      │
                  │ EJB Beans:           │
                  │ - MovieServiceBean   │
                  │ - AuthServiceBean    │
                  └──────────┬───────────┘
                             │ JDBC
                             ↓
                  ┌──────────────────────┐
                  │   PostgreSQL         │
                  │   Port: 5432         │
                  └──────────────────────┘
```

## Описание компонентов

### 1. Frontend (Vue.js)
- **Технологии**: Vue.js 3, Vite
- **Порт**: 5173 (dev)
- **Назначение**: Пользовательский интерфейс
- **Взаимодействие**: Отправляет HTTPS запросы к API Gateway

### 2. API Gateway (Spring Cloud Gateway)
- **Технологии**: Spring Cloud Gateway, Spring Boot
- **Порт**: 8443 (HTTPS)
- **Назначение**: Единая точка входа, маршрутизация, CORS
- **Маршруты**:
  - `/caller/**` → caller-service
  - `/called/**` → called-adapter
- **Дополнительно**: SSL/TLS, Service Discovery (Eureka)

### 3. caller-service (Spring Boot)
- **Технологии**: Spring Boot, REST
- **Назначение**: Бизнес-логика перераспределения наград между жанрами
- **API**: 
  - `POST /caller/redistribute-rewards/{fromGenre}/{toGenre}`
- **Взаимодействие**: Вызывает REST API called-adapter через API Gateway

### 4. called-adapter (Spring Boot REST Adapter)
- **Технологии**: Spring Boot, JAX-WS (SOAP Client)
- **Порт**: 8090
- **Назначение**: REST-прослойка для SOAP сервисов
- **REST API**:
  - Movies: `/api/movies/**`
  - Auth: `/api/auth/**`
- **SOAP Clients**:
  - `MovieSoapClient` - вызывает MovieSoapService
  - `AuthSoapClient` - вызывает AuthSoapService
- **Конфигурация**: `soap.service.url=http://payara-2:8080`

### 5. called-service (Payara + EJB + SOAP)
- **Технологии**: Java EE 8, EJB 3.2, JAX-WS (SOAP), JPA
- **Порт**: 8080
- **Назначение**: Бизнес-логика и персистентность
- **SOAP Web Services**:
  - `MovieSoapService` - CRUD операции с фильмами
    - WSDL: `http://payara-2:8080/MovieService?wsdl`
  - `AuthSoapService` - регистрация и аутентификация
    - WSDL: `http://payara-2:8080/AuthService?wsdl`
- **EJB Beans**:
  - `MovieServiceBean` - бизнес-логика фильмов
  - `AuthServiceBean` - бизнес-логика аутентификации
- **Развертывание**: Автодеплой через `autodeploy/`

### 6. PostgreSQL
- **Порт**: 5432
- **База данных**: `called_db`
- **Пользователь**: `called_user`
- **Назначение**: Хранение данных (фильмы, пользователи)

## Поток данных

### Пример: Создание фильма

```
1. Frontend → API Gateway
   POST https://localhost:8088/called/api/movies
   Body: MovieCreateRequest (JSON)

2. API Gateway → called-adapter
   POST http://called-adapter:8090/api/movies
   Body: MovieCreateRequest (JSON)

3. called-adapter → called-service
   SOAP Request to http://payara-2:8080/MovieService
   Method: createMovie
   Body: MovieCreateRequest (XML/SOAP)

4. called-service (MovieSoapService)
   ↓ Делегирует вызов
   MovieServiceBean (EJB)
   ↓ JPA
   PostgreSQL

5. Ответ идет обратно по цепочке:
   PostgreSQL → EJB → SOAP Service → called-adapter → API Gateway → Frontend
```

### Пример: Перераспределение наград

```
1. Frontend → API Gateway
   POST https://localhost:8088/caller/redistribute-rewards/ACTION/COMEDY
   Header: Authorization: Bearer <token>

2. API Gateway → caller-service
   POST http://caller-service/caller/redistribute-rewards/ACTION/COMEDY

3. caller-service → API Gateway → called-adapter → called-service
   - Два поисковых запроса (fromGenre и toGenre)
   - Обновление всех фильмов batch

4. caller-service получает ответы и возвращает результат
```

## Преимущества новой архитектуры

### 1. Слабая связанность
- called-adapter изолирует SOAP от REST клиентов
- Изменения в SOAP сервисе не влияют на REST API

### 2. Независимое масштабирование
- Каждый компонент может масштабироваться отдельно
- called-adapter можно реплицировать для load balancing

### 3. Технологическая независимость
- Сервисы используют стандартные протоколы (REST, SOAP)
- Легко заменить или добавить компоненты

### 4. Простота миграции
- Существующие клиенты работают без изменений
- REST API остался прежним

## Недостатки и компромиссы

### 1. Дополнительный слой
- called-adapter добавляет latency
- Еще один компонент для мониторинга

### 2. Сложность отладки
- Больше точек отказа
- Сложнее трассировать запросы

### 3. Преобразование данных
- Двойное преобразование: JSON ↔ Java ↔ XML
- Потенциальная потеря производительности

## Мониторинг и отладка

### Логи компонентов

```bash
# called-adapter
docker logs called-adapter

# called-service (Payara)
docker logs payara-2

# API Gateway
docker logs api-gateway

# caller-service
docker logs caller-service
```

### Проверка WSDL

```bash
# MovieService WSDL
curl http://localhost:8080/MovieService?wsdl

# AuthService WSDL
curl http://localhost:8080/AuthService?wsdl
```

### Проверка endpoint'ов

```bash
# Через called-adapter напрямую
curl http://localhost:8090/api/movies/search -X POST \
  -H "Content-Type: application/json" -d '{}'

# Через API Gateway
curl -k https://localhost:8088/called/api/movies/search -X POST \
  -H "Content-Type: application/json" -d '{}'
```

## Будущие улучшения

### 1. Service Mesh (Istio/Linkerd)
- Централизованное управление трафиком
- Автоматический retry, circuit breaker
- Distributed tracing

### 2. API Gateway Features
- Rate limiting
- Authentication на уровне gateway
- Request/Response transformation

### 3. Кэширование
- Redis для кэширования SOAP ответов
- Снижение нагрузки на called-service

### 4. Мониторинг
- Prometheus + Grafana
- Distributed tracing (Jaeger, Zipkin)
- Health checks

### 5. CI/CD
- Автоматическая сборка и деплой
- Integration tests
- Contract testing для SOAP

## Mule ESB интеграция

Для интеграции Mule ESB:

```
Frontend → API Gateway → Mule ESB → called-service (SOAP)
                             ↓
                       caller-service
```

**Mule ESB будет**:
- Принимать REST запросы
- Вызывать SOAP сервисы
- Обеспечивать трансформацию данных
- Маршрутизацию запросов
- Оркестрацию бизнес-процессов

В этом случае called-adapter может быть заменен на Mule ESB или работать параллельно для A/B тестирования.
