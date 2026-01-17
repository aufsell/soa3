# Миграция на SOAP архитектуру

## Обзор изменений

Проект был переработан для использования SOAP вместо прямых EJB вызовов:

### Новая архитектура

```
Клиент (Frontend)
    ↓
API Gateway (Spring Cloud Gateway)
    ↓
called-adapter (Spring Boot REST API)
    ↓ (SOAP)
called-service (Payara EJB + SOAP Web Services)
    ↓
PostgreSQL
```

### Основные изменения

1. **called-service (called-ejb)** - переписан для поддержки SOAP
   - Добавлены SOAP Web Service endpoints: `MovieSoapService`, `AuthSoapService`
   - EJB beans остались без изменений
   - SOAP сервисы делегируют вызовы к EJB beans
   - Все DTO классы аннотированы для JAXB сериализации

2. **called-adapter** - новый REST адаптер на Spring Boot
   - Предоставляет тот же REST API, что был в called-web
   - Внутри использует SOAP клиенты для вызова called-service
   - Обеспечивает работу caller-service и frontend без изменений
   - Порт: 8090

3. **API Gateway** - обновлена маршрутизация
   - Маршрут `/called/**` теперь направляется на called-adapter
   - Маршрут `/caller/**` остался без изменений

4. **caller-service** - без изменений
   - Продолжает работать как раньше
   - Вызывает REST API через API Gateway

## Сборка и запуск

### 1. Собрать called-ejb

```bash
cd called-service
./gradlew :called-ejb:jar
```

### 2. Скопировать JAR для автодеплоя

```bash
cp called-ejb/build/libs/called-ejb.jar ../payara2/called-ejb.jar
```

Или использовать скрипт:

```bash
cd payara2
chmod +x build-and-copy.sh
./build-and-copy.sh
```

### 3. Собрать Spring Cloud сервисы

```bash
cd spring-cloud
./gradlew build
```

### 4. Запустить Docker Compose

```bash
docker-compose up --build
```

## Структура компонентов

### called-service (EJB + SOAP)

**SOAP Web Services:**
- `MovieSoapService` - операции с фильмами
  - WSDL: `http://payara-2:8080/MovieService?wsdl`
- `AuthSoapService` - аутентификация
  - WSDL: `http://payara-2:8080/AuthService?wsdl`

**EJB Beans:**
- `MovieServiceBean` - бизнес-логика работы с фильмами
- `AuthServiceBean` - бизнес-логика аутентификации

### called-adapter (REST → SOAP)

**REST Controllers:**
- `MovieController` - `/api/movies/**`
- `AuthController` - `/api/auth/**`

**SOAP Clients:**
- `MovieSoapClient` - клиент для вызова MovieSoapService
- `AuthSoapClient` - клиент для вызова AuthSoapService

**Конфигурация:**
- `soap.service.url: http://payara-2:8080`

## Endpoints

### Через API Gateway (https://localhost:8088)

**Movies:**
- POST `/called/api/movies` - создать фильм
- GET `/called/api/movies/{id}` - получить фильм
- PUT `/called/api/movies` - обновить несколько фильмов
- PUT `/called/api/movies/{id}` - обновить один фильм
- DELETE `/called/api/movies/{id}` - удалить фильм
- POST `/called/api/movies/search` - поиск фильмов

**Auth:**
- POST `/called/api/auth/register` - регистрация
- POST `/called/api/auth/login` - вход

**Caller Service:**
- POST `/caller/redistribute-rewards/{fromGenre}/{toGenre}` - перераспределение наград

### Прямые SOAP endpoints (для отладки)

- MovieService WSDL: `http://localhost:8080/MovieService?wsdl`
- AuthService WSDL: `http://localhost:8080/AuthService?wsdl`

## Проверка работоспособности

### 1. Проверить SOAP WSDL

```bash
curl http://localhost:8080/MovieService?wsdl
curl http://localhost:8080/AuthService?wsdl
```

### 2. Проверить called-adapter

```bash
curl http://localhost:8090/api/movies/search -X POST -H "Content-Type: application/json" -d '{}'
```

### 3. Проверить через API Gateway

```bash
# Регистрация
curl -k https://localhost:8088/called/api/auth/register -X POST \
  -H "Content-Type: application/json" \
  -d '{"email":"test@test.com","password":"password","name":"Test User"}'

# Вход
curl -k https://localhost:8088/called/api/auth/login -X POST \
  -H "Content-Type: application/json" \
  -d '{"email":"test@test.com","password":"password"}'
```

## Примечания

1. **SOAP сервисы** автоматически обнаруживаются Payara через аннотации `@WebService`
2. **Автодеплой** работает через директорию `autodeploy/` в Payara
3. **Datasource** конфигурируется через post-boot-commands.asadmin
4. **JAXB** используется для сериализации XML в SOAP
5. **JAX-WS** используется для создания SOAP клиентов

## Mule ESB (для будущей интеграции)

Для настройки Mule ESB:
1. Установить Mule ESB Runtime
2. Создать Mule приложение для интеграции
3. Настроить SOAP connectors для вызова called-service
4. Настроить HTTP listeners для REST API
5. При необходимости добавить трансформации данных

## Troubleshooting

### SOAP сервис недоступен
- Проверить логи Payara: `docker logs payara-2`
- Убедиться, что called-ejb.jar развернут: `docker exec payara-2 ls /opt/payara/appserver/glassfish/domains/domain1/autodeploy/`

### called-adapter не может подключиться к SOAP
- Проверить, что payara-2 запущен и доступен
- Проверить URL SOAP сервиса в application.yml
- Проверить логи: `docker logs called-adapter`

### API Gateway не маршрутизирует запросы
- Проверить конфигурацию в spring-config-repo/api-gateway.yml
- Перезапустить API Gateway: `docker restart api-gateway`
