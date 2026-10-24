# Microservices BFF

Три Spring Boot микросервиса, демонстрирующих паттерн **Backend-for-Frontend (BFF)**.

BFF принимает запрос клиента, параллельно вызывает два backend-сервиса (пользователи и заказы), агрегирует ответы в один объект и возвращает клиенту. 

---

## Архитектура

```
┌─────────────┐
│   Клиент    │
└──────┬──────┘
       │  GET /api/site-bff/user/{userId}
       ▼
┌─────────────────────┐
│     bff-service     │  :8080
│  (агрегация данных) │
└──────┬───────┬──────┘
       │       │
       │       │  GET /api/orders/by-user/{userId}
       │       └──────────────────────────┐
       │  GET /api/users/{userId}         │
       ▼                                  ▼
┌──────────────────┐            ┌──────────────────┐
│   user-service   │  :8081     │  order-service   │  :8082
│  (пользователи)  │            │     (заказы)     │
└──────────────────┘            └──────────────────┘
```

| Сервис | Порт | Назначение | Endpoint |
|---|---|---|---|
| `user-service` | 8081 | Информация о пользователях | `GET /api/users/{userId}` |
| `order-service` | 8082 | Заказы пользователей | `GET /api/orders/by-user/{user}` |
| `bff-service` | 8080 | Агрегация для клиента | `GET /api/site-bff/user/{userId}` |

---

## Структура проекта

```
microservices-bff/
├── pom.xml                 # родительский (aggregator) pom
├── README.md
├── user-service/
│   ├── pom.xml
│   └── src/main/
│       ├── java/ru/netology/user_service/
│       │   ├── UserServiceApplication.java
│       │   ├── controller/UserController.java
│       │   ├── model/User.java
│       │   └── repository/UserRepository.java
│       └── resources/application.properties
├── order-service/
│   ├── pom.xml
│   └── src/main/
│       ├── java/ru/netology/order_service/
│       │   ├── OrderServiceApplication.java
│       │   ├── controller/OrderController.java
│       │   ├── model/Order.java
│       │   ├── model/OrderItem.java
│       │   └── repository/OrderRepository.java
│       └── resources/application.properties
└── bff-service/
    ├── pom.xml
    └── src/main/
        ├── java/ru/netology/bffservice/
        │   ├── BffServiceApplication.java
        │   ├── config/RestClientConfig.java
        │   ├── controller/SiteBffController.java
        │   ├── dto/UserDto.java
        │   ├── dto/OrderDto.java
        │   ├── dto/OrderItemDto.java
        │   ├── dto/UserProfileDto.java
        │   └── service/UserAggregationService.java
        └── resources/application.properties
```

---

## Технологии

- **Java 17** (Eclipse Temurin)
- **Spring Boot 3.2.5**
- **Maven** (multi-module)
- **Lombok** 1.18.48
- **RestClient** (Spring 6.1) для HTTP-вызовов между сервисами
- **CompletableFuture** для параллельных запросов из BFF

---

## Модели данных

### User (user-service)

| Поле | Тип | Описание |
|---|---|---|
| `id` | Long | Идентификатор |
| `fullName` | String | ФИО |
| `deliveryAddress` | String | Адрес доставки |
| `phoneNumber` | String | Номер телефона |
| `email` | String | Email |

### Order (order-service)

| Поле | Тип | Описание |
|---|---|---|
| `id` | Long | Идентификатор заказа |
| `userId` | Long | Идентификатор пользователя |
| `totalAmount` | Double | Сумма заказа |
| `currency` | String | Валюта |
| `items` | List&lt;OrderItem&gt; | Позиции заказа |

### OrderItem

| Поле | Тип | Описание |
|---|---|---|
| `productName` | String | Название товара |
| `quantity` | Integer | Количество |
| `price` | Double | Цена |

### UserProfileDto (bff-service) — агрегированная модель

| Поле | Тип | Описание |
|---|---|---|
| `user` | UserDto | Данные пользователя |
| `orders` | List&lt;OrderDto&gt; | Заказы пользователя |

---

## Запуск

### Требования

- JDK 17+
- Maven 3.9+ (или встроенный Maven в IntelliJ IDEA)



### Запуск сервисов

Запускать **в таком порядке**, каждый в отдельном окне терминала / Run-конфигурации IDEA:

```bash
# 1. user-service — порт 8081


# 2. order-service — порт 8082


# 3. bff-service — порт 8080

```



---

## Проверка



```
curl http://localhost:8081/api/users/1
curl http://localhost:8082/api/orders/by-user/1
```

### Через BFF

```bash
curl http://localhost:8080/api/site-bff/user/1
```

**Ожидаемый ответ:**

```json
{
  "user": {
    "id": 1,
    "fullName": "Иванов Иван Иванович",
    "deliveryAddress": "г. Москва, ул. Ленина, д. 1, кв. 10",
    "phoneNumber": "+7-900-111-22-33",
    "email": "ivanov@example.com"
  },
  "orders": [
    {
      "id": 101,
      "userId": 1,
      "totalAmount": 1500.0,
      "currency": "RUB",
      "items": [
        { "productName": "Книга", "quantity": 1, "price": 500.0 },
        { "productName": "Ручка", "quantity": 2, "price": 500.0 }
      ]
    },
    {
      "id": 102,
      "userId": 1,
      "totalAmount": 3200.0,
      "currency": "RUB",
      "items": [
        { "productName": "Монитор", "quantity": 1, "price": 3000.0 },
        { "productName": "Кабель HDMI", "quantity": 1, "price": 200.0 }
      ]
    }
  ]
}
```

### Тестовые данные

| userId | ФИО | Заказы |
|---|---|---|
| 1 | Иванов Иван Иванович | 2 заказа (RUB) |
| 2 | Петров Пётр Петрович | 1 заказ (RUB) |
| 3 | Сидорова Анна Сергеевна | 1 заказ (USD) |

---

## Как работает BFF

`UserAggregationService` при получении `userId`:

1. **Параллельно** запускает два HTTP-запроса через `CompletableFuture`:
    - `GET http://localhost:8081/api/users/{userId}` → `UserDto`
    - `GET http://localhost:8082/api/orders/by-user/{userId}` → `List<OrderDto>`
2. Ждёт завершения обоих (`CompletableFuture.allOf(...).join()`).
3. Собирает `UserProfileDto { user, orders }`.
4. Возвращает клиенту.



URL'ы backend-сервисов задаются в `bff-service/src/main/resources/application.properties`:

```properties
services.user-url=http://localhost:8081
services.order-url=http://localhost:8082
```

---

## Реализованные требования

- ✅ `user-service`: `@RestController`, `GET /api/users/{userId}`
- ✅ `order-service`: `@RestController`, `GET /api/orders/by-user/{user}`
- ✅ `bff-service`: `@RestController`, `GET /api/site-bff/user/{userId}`
- ✅ BFF не хранит данные — только вызывает и агрегирует
- ✅ Общая (агрегированная) модель `UserProfileDto` в BFF
- ✅ Модели `User` и `Order` содержат все требуемые поля

---

