# Система управления задачами(тестовое задание)

## Стек используемых технологий:

* Java 17
* Spring Boot 3.2.0
* Spring Security
* JUnit
* PostgreSQL
* Liquibase
* Docker

## Описание приложения

Разработана система управления задачами (Task Management System) с использованием Java. Система обеспечивает создание,
редактирование, удаление и просмотр задач. Каждая задача содержит
заголовок, описание, статус (например, "в ожидании", "в процессе", "завершено") и приоритет (например, "высокий", "
средний", "низкий"), а также автора задачи и исполнителя.

## Модель базы данных представлена на ER-диаграмме:

![Модель базы данных](src/main/resources/Schema.png)

## Руководство по локальному запуску и работе с приложением

1) Использовать ```git clone``` данного репозитория к себе на устройство
2) Запустить среду разработки ```Intellij Idea``` или другую аналогичную
3) Приложение также поддерживает запуск через Docker с помощью команды ```docker-compose up```, которую можно написать в
   терминале среды разработки
4) Приложение развернуто и готово принимать запросы по адресу: ```http://localhost:8080``` в json-формате

##Список URL HTTP-методов

### ```POST /signup```

Регистрирует нового пользователя в системе. Пример запроса:

```json
{
   "name": "bob",
   "email": "bob@mail.com",
   "password": "111111"
}
```

### ```POST /signin```

Авторизация уже зарегестрированного пользователя, для получения JWT-токена, с помощью которого осуществляется доступ к 
остальным эндпоинтам Пример запроса:

```json
{
   "email": "bob@mail.com",
   "password": "111111"
}
```

Ответом на который будет JWT-токен в формате:

```json
{
   "jwt": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJib2JAbWFpbC5jb20iLCJyb2xlcyI6WyJVU0VSIl0sImV4cCI6MTcwMTk2NDUwMCwiaWF0IjoxNzAxOTYyNzAwfQ.1IYltM5Er_XOGf5Cr8zXOyu_135e_HoKjW9vLxyAEoo"
}
```

### ```POST /tasks/create```

Запрос на создание новой задачи. Пример запроса:

```json
{
   "creator_id": 1,
   "title": "string",
   "description": "string",
   "status": "IN_WAITING",
   "priority": "LOW"
}
```

### ```PATCH /tasks/{creatorId}/update/{taskId}```

Запрос от создателя задачи на ее изменение. Пример запроса:

```json
{
   "title": "string",
   "description": "string",
   "status": "IN_PROGRESS",
   "priority": "HIGH",
   "executor_id": 2
}
```

### ```PATCH /tasks/update/{executorId}/{taskId}```

Запрос на обновление статуса задачи от исполнителя. Пример запроса:

```json
{
   "status": "COMPLETED"
}
```

### ```GET /tasks/{taskId}```

Запрос на получение задачи по ее id.

### ```DELETE /tasks/{taskId}/{creatorId}```

Запрос на удаление задачи ее создателем.

### ```GET /tasks/creator/{creatorId}```

Запрос на получение задач определенного создателя. Добавлена сортировка по статусу и пагинация. Пример строки запроса:
```http://localhost:8080/tasks/creator/1?sort=STATUS&from=0&size=10``` - запрос на вывод 10 задач с сортировкой по статусу.

### ```GET /tasks/executor/{creatorId}```

Запрос на получение задач определенного исполнителя. Добавлена сортировка по статусу и пагинация. Пример строки запроса:
```http://localhost:8080/tasks/executor/1?sort=STATUS&from=0&size=10``` - запрос на вывод 10 задач с сортировкой по статусу.

### ```POST /tasks/comments/{taskId}/{userId}```

Запрос на добавление комментария к задаче от определенного пользователя. Пример запроса:

```json
{
   "text": "Task comment"
}
```

### ```PATCH /tasks/comments/{comId}/{userId}```

Запрос на изменение комментария его автором. Пример запроса:

```json
{
   "text": "Updated comment"
}
```
### ```DELETE /tasks/comments/{comId}/{userId}```

Запрос на удаление пользователем своего комментария.


