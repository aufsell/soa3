# SOA3 - Сервисно-ориентированная архитектура

## Описание проекта

Проект состоит из двух основных частей:
1. **Called Service** (Вызываемый сервис) - Java EE приложение на Payara с SOAP и REST API
2. **Spring Cloud** (Вызывающий сервис) - микросервисная архитектура на Spring Boot

---

## 🚀 Быстрый старт

### 1. Сборка проекта

```bash
# Сборка called-service (EJB + Web)
cd called-service
./gradlew clean build -x test

# Копирование артефактов для Docker
cp called-ejb/build/libs/called-ejb.jar ../payara2/
cp called-web/build/libs/called.war ../payara1/

# Сборка spring-cloud сервисов
cd ../spring-cloud
./gradlew clean build -x test
```

### 2. Запуск всех сервисов

```bash
cd /path/to/soa3
docker-compose up -d --build
```

### 3. Проверка работоспособности

```bash
# Проверка SOAP
curl "http://localhost:8081/called/MovieWebService?wsdl"

# Проверка REST
curl http://localhost:8081/called/api/ping

# Проверка Spring Cloud Gateway
curl http://localhost:8443/api/caller/ping
```

---

## 📦 Архитектура

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              DOCKER NETWORK                                  │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  ┌─────────────┐     ┌─────────────┐     ┌─────────────────────────────┐    │
│  │  Frontend   │     │ API Gateway │     │      Eureka Server          │    │
│  │  (Vue.js)   │────▶│   :8443     │────▶│        :8761                │    │
│  │   :80       │     │  (Spring)   │     │  (Service Discovery)        │    │
│  └─────────────┘     └──────┬──────┘     └─────────────────────────────┘    │
│                             │                                                │
│                             ▼                                                │
│                    ┌─────────────────┐                                       │
│                    │ Caller Service  │                                       │
│                    │    :8082        │                                       │
│                    │  (Spring Boot)  │                                       │
│                    └────────┬────────┘                                       │
│                             │                                                │
│         ┌───────────────────┼───────────────────┐                           │
│         │                   │                   │                           │
│         ▼                   ▼                   ▼                           │
│  ┌─────────────┐    ┌─────────────┐    ┌─────────────────────────────┐     │
│  │  Payara-1   │    │  Payara-2   │    │      PostgreSQL             │     │
│  │  (Web/SOAP) │───▶│   (EJB)     │───▶│       :5432                 │     │
│  │   :8081     │    │   :8080     │    │   (called_db)               │     │
│  │             │    │             │    │                             │     │
│  │ - REST API  │    │ - MovieSvc  │    └─────────────────────────────┘     │
│  │ - SOAP API  │    │ - AuthSvc   │                                        │
│  │             │    │ - PingSvc   │    ┌─────────────────────────────┐     │
│  └─────────────┘    │ - HelloSvc  │    │        Consul               │     │
│                     │             │    │       :8500                 │     │
│                     │ EJB Invoker │    │  (Service Registry)         │     │
│                     │ /ejb-invoker│    └─────────────────────────────┘     │
│                     └─────────────┘                                        │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 🔗 Endpoints

### Called Service - SOAP API (Payara-1)

| Endpoint | URL |
|----------|-----|
| **WSDL** | `http://localhost:8081/called/MovieWebService?wsdl` |
| **SOAP Endpoint** | `http://localhost:8081/called/MovieWebService` |

**Доступные SOAP операции:**
- `ping` - проверка связи с EJB
- `createMovie` - создание фильма
- `getMovieById` - получение фильма по ID
- `updateMovie` - обновление фильма
- `updateMovies` - массовое обновление (PUT)
- `deleteMovie` - удаление фильма
- `searchMovies` - поиск фильмов с пагинацией
- `login` - авторизация
- `register` - регистрация

**Пример SOAP запроса (ping):**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
               xmlns:tns="http://soap.web.calledweb.lovesoa.org/">
  <soap:Body>
    <tns:ping/>
  </soap:Body>
</soap:Envelope>
```

```bash
curl -X POST "http://localhost:8081/called/MovieWebService" \
  -H "Content-Type: text/xml; charset=utf-8" \
  -H "SOAPAction: ping" \
  -d '<?xml version="1.0"?><soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" xmlns:tns="http://soap.web.calledweb.lovesoa.org/"><soap:Body><tns:ping/></soap:Body></soap:Envelope>'
```

---

### Called Service - REST API (Payara-1)

| Метод | Endpoint | Описание |
|-------|----------|----------|
| GET | `http://localhost:8081/called/api/ping` | Проверка связи |
| GET | `http://localhost:8081/called/api/movies` | Список фильмов |
| GET | `http://localhost:8081/called/api/movies/{id}` | Получить фильм |
| POST | `http://localhost:8081/called/api/movies` | Создать фильм |
| PATCH | `http://localhost:8081/called/api/movies/{id}` | Обновить фильм |
| DELETE | `http://localhost:8081/called/api/movies/{id}` | Удалить фильм |
| POST | `http://localhost:8081/called/api/auth/login` | Авторизация |
| POST | `http://localhost:8081/called/api/auth/register` | Регистрация |

---

### EJB HTTP Invoker (Payara-2)

| Endpoint | URL |
|----------|-----|
| **EJB Invoker** | `http://localhost:8080/ejb-invoker` |

Используется для удалённого вызова EJB из Payara-1.

---

### Spring Cloud Services

| Сервис | URL | Описание |
|--------|-----|----------|
| **Eureka Dashboard** | `http://localhost:8761` | UI для просмотра зарегистрированных сервисов |
| **API Gateway** | `https://localhost:8443` | Точка входа для всех запросов |
| **Config Server** | `http://localhost:8888` | Централизованная конфигурация |
| **Caller Service** | `http://localhost:8082` | Вызывающий сервис |

**Примеры запросов через API Gateway:**
```bash
# Ping через caller-service
curl -k https://localhost:8443/api/caller/ping

# Получить фильмы (проксирование к called-service)
curl -k https://localhost:8443/api/movies
```

---

### Служебные сервисы

| Сервис | URL | Описание |
|--------|-----|----------|
| **Consul UI** | `http://localhost:8500` | Service Discovery для Payara |
| **PostgreSQL** | `localhost:5432` | База данных |

**Подключение к PostgreSQL:**
```bash
psql -h localhost -p 5432 -U called_user -d called_db
# Пароль: called_pass
```

---

## 🐳 Docker Compose команды

```bash
# Запустить всё
docker-compose up -d

# Запустить только called-service (Payara)
docker-compose up -d payara-1 payara-2 postgres consul

# Запустить только spring-cloud
docker-compose up -d eureka-server config-server api-gateway caller-service

# Пересобрать и запустить
docker-compose up -d --build

# Посмотреть логи
docker-compose logs -f payara-1
docker-compose logs -f payara-2
docker-compose logs -f caller-service

# Остановить всё
docker-compose down

# Остановить и удалить volumes
docker-compose down -v
```

---

## 📁 Структура проекта

```
soa3/
├── called-service/           # Вызываемый сервис (Java EE)
│   ├── called-ejb/           # EJB модуль (бизнес-логика)
│   │   └── src/main/java/
│   │       └── org/lovesoa/calledejb/
│   │           ├── entity/           # JPA сущности
│   │           ├── repository/       # DAO слой
│   │           ├── service/          # EJB бины
│   │           │   ├── api/          # Remote интерфейсы
│   │           │   └── impl/         # Реализации
│   │           └── config/           # DataSource конфиг
│   │
│   └── called-web/           # Web модуль (контроллеры)
│       └── src/main/java/
│           └── org/lovesoa/calledweb/
│               ├── web/
│               │   ├── rest/         # REST контроллеры
│               │   ├── soap/         # SOAP сервис ⭐
│               │   │   ├── MovieSoapService.java
│               │   │   ├── SoapDtoConverter.java
│               │   │   └── dto/      # SOAP DTO с JAXB
│               │   └── RemoteClient/ # EJB клиенты
│               └── dto/              # REST DTO
│
├── spring-cloud/             # Вызывающий сервис (Spring Boot)
│   ├── api-gateway/          # API Gateway
│   ├── caller-service/       # Сервис-вызыватель
│   ├── config-server/        # Config Server
│   ├── eureka-server/        # Service Discovery
│   └── spring-config-repo/   # Конфигурации
│
├── payara1/                  # Docker контекст для Web
│   ├── Dockerfile
│   └── called.war
│
├── payara2/                  # Docker контекст для EJB
│   ├── Dockerfile
│   ├── called-ejb.jar
│   ├── post-boot-commands.asadmin
│   └── *.jar                 # Библиотеки (JDBC, Jackson, JWT)
│
├── frontend/                 # Vue.js фронтенд
├── TLS/                      # SSL сертификаты
└── docker-compose.yml        # Оркестрация
```

---

## ⚙️ Конфигурация

### Payara-2 (EJB Server)
- **post-boot-commands.asadmin** - включение EJB HTTP Invoker:
  ```
  set-ejb-invoker-configuration --enabled=true --securityenabled=false --endpoint=/ejb-invoker
  ```

### Called Service - Remote EJB Client
- URL для lookup: `http://payara-2:8080/ejb-invoker`
- JNDI имена: `java:global/called-ejb/MovieServiceBean!org.lovesoa.calledejb.service.api.MovieServiceRemote`

---

## 🔧 Troubleshooting

### SOAP возвращает 500
Проверьте логи payara-1:
```bash
docker logs payara-1 2>&1 | grep -i error
```

### EJB lookup fails
1. Убедитесь что payara-2 запущен и EJB задеплоен:
   ```bash
   docker logs payara-2 2>&1 | grep "successfully deployed"
   ```
2. Проверьте EJB invoker:
   ```bash
   curl http://localhost:8080/ejb-invoker
   ```

### База данных не подключается
Проверьте что postgres запущен:
```bash
docker-compose ps postgres
docker logs called-postgres
```

---

## 📝 Mule ESB Integration

Для интеграции с Mule ESB (Anypoint Studio):

1. **Импортируйте WSDL:**
   ```
   http://localhost:8081/called/MovieWebService?wsdl
   ```

2. **Настройте Web Service Consumer** в Mule flow

3. **Пример конфигурации:**
   - Service: `MovieWebService`
   - Port: `MoviePort`
   - Address: `http://payara-1:8080/called/MovieWebService` (внутри Docker)
   - Или: `http://localhost:8081/called/MovieWebService` (снаружи)

---

## 📌 Важные порты

| Порт | Сервис | Протокол |
|------|--------|----------|
| 80 | Frontend (nginx) | HTTP |
| 5432 | PostgreSQL | TCP |
| 8080 | Payara-2 (EJB) | HTTP |
| 8081 | Payara-1 (Web) | HTTP |
| 8082 | Caller Service | HTTP |
| 8443 | API Gateway | HTTPS |
| 8500 | Consul | HTTP |
| 8761 | Eureka | HTTP |
| 8888 | Config Server | HTTP |
