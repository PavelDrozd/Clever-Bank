# Проект: Clever-Банк

## Clever-Bank - это простая банковская система, которая позволяет клиентам вносить и снимать деньги со своих счетов, а также переводить деньги другим клиентам Clever-Bank и клиентам других банков. Система обеспечивает безопасность операций и работает в многопоточной среде.

### Реализованные операции

- Пополнение счета
- Вывод средств со счета
- Денежный перевод другому клиенту
- Регулярная проверка и начисление процентов на остаток средств на счете в конце месяца
- Выписка чека после транзакции

### Используемые технологии

- Java 17
- Gradle
- Lombok
- PostgreSQL
- HikariCP
- Javax Servlet
- SnakeYaml
- ModelMapper
- GSON
- Slf4j
- Logback

### Инструкция по запуску проекта:

- Создать базу данных в PostgreSQL с названием "bankmanager"
  (все конфигурационные параметры можно изменить в файле application.yml - src/main/resources/application.yml)
- запустить SQL скрипт файл с schema.sql - sql/schema.sql (для тестовых данных можно запустить data.sql)
- открыть консоль в корневой папке проекта и прописать команду: gradle build war
- зайти в папку build/libs и переместить/скопировать файл: clever.war в папку tomcat/webapps
- далее в папке tomcat/bin в зависимости от системы запустить скрипт: startup.bat (Windows), startup.sh (Unix systems)
- подождать запуска Tomcat сервера и по ссылке localhost:8080 запуститься начальная страница

### CRUD операции:

### Get by ID:

- послать GET запрос на ссылку: localhost:8080/clever/api/{entity}?id={ID}
- {entity} написать название сущности: account, bank, transaction, user
- {ID} написать значение ID объекта
- результатом ожидается JSON объект выбранной сущности

##### Пример Get by ID:

- Отправляем: http://localhost:8080/clever/api/user?id=1
- Ответ сервера: { "id": 1, "firstName": "Aleksandra",  "lastName": "Fedorova" }

### Get all:

- послать GET запрос по ссылке: localhost:8080/clever/api/{entities}
- {entities} написать название сущностей: accounts, banks, transactions, users
- результатом ожидается JSON список объектов выбранной сущности

##### Пример Get all:

- Отправляем: http://localhost:8080/clever/api/banks
- Ответ сервера: [ { "id": 1, "name": "Clever-Bank" }, { "id": 2, "name": "IDEA bank" },
  { "id": 3, "name": "Trust bank" }, { "id": 4, "name": "Just in time bank" },
  { "id": 5, "name": "Bank Individual" } ]

### Create:

- послать POST запрос по ссылке: localhost:8080/clever/api/create_{entity}
- {entity} написать название сущности: account, bank, transaction, user
- в запросе отправить JSON формат создаваемого объекта с заполнением полей кроме поля ID основной сущности
- результатом ожидается созданный объект

#### Список параметров для заполнения при create операции:

- в круглых скобках тип данных для заполнения
- account : { "bank": { "id": (Long) }, "user": { "id": (Long) } }
- bank : { "name": (String) }
- transaction: { "amount": (Double), "senderAccount": { "id": (Long) }, "recipientAccount": { "id": (Long) } }
- user : { "firstName": (String), "lastName": (String) }

##### Пример Create:

- Отправляем POST запрос с телом { "firstName": "Anatolii", "lastName": "Filatov" }
  на : http://localhost:8080/clever/api/create_user
- Ответ сервера: { "id": 21, "firstName": "Anatolii", "lastName": "Filatov" }

### Update:

- послать PUT запрос по ссылке: localhost:8080/clever/api/update_{entity}
- {entity} написать название сущности: account, bank, transaction, user
- результатом ожидается обновленный объект

#### Список параметров для заполнения при update операции:

- в круглых скобках тип данных для заполнения
- account : { "id": (Long), "number": (Long), "amount": (Double), "bank": { "id": (Long) }, "user": { "id": (Long) } }
- bank : { "id": (Long), "name": (String) }
- transaction: { "id": (Long), "amount": (Double), "senderAccount": { "id": (Long) }, "recipientAccount": { "id": (
  Long) } }
- user : { "id": (Long), "firstName": (String), "lastName": (String) }

##### Пример Update:

- Отправляем PUT запрос с телом { "id": 5,  "name": "Bank UNION" }
  на : http://localhost:8080/clever/api/update_bank
- Ответ сервера: { "id": 5, "name": "Bank UNION" }

### Delete:

- послать DELETE запрос с телом { "id": (Long) }
  на : localhost:8080/clever/api/delete_{entity}
- {entity} написать название сущности: account, bank, transaction, user
- результатом ожидается код 204 без тела ответа

##### Пример Delete:

- Отправляем DELETE запрос с телом { "id":5 }
  на: http://localhost:8080/clever/api/delete_transaction
- Ответ сервера: 204 No content

### Deposit:

- послать POST запрос с телом { "id": (Long), "value": (Double) }
  на : localhost:8080/clever/api/deposit
- результатом ожидается объект которому была начислена сумма

##### Пример Deposit:

- Отправляем POST запрос с телом { "id": 1,  "value": 1000 }
  на : http://localhost:8080/clever/api/deposit
- Ответ сервера: { "id": 1, "number": 3273630263392, "amount": 2000.0, "cashbackLastDate": "30-08-2023",
  "bank": { "id": 1, "name": "Clever-Bank" },
  "user": { "id": 1, "firstName": "Aleksandra", "lastName": "Fedorova" } }

### Withdraw:

- послать POST запрос с телом { "id": (Long), "value": (Double) }
  на : localhost:8080/clever/api/withdraw
- результатом ожидается объект у которого была снята сумма

##### Пример Withdraw:

- Отправляем POST запрос с телом { "id": 1,  "value": 1500 }
  на : http://localhost:8080/clever/api/withdraw
- Ответ сервера: { "id": 1, "number": 3273630263392, "amount": 500.0, "cashbackLastDate": "30-08-2023",
  "bank": { "id": 1, "name": "Clever-Bank" },
  "user": { "id": 1, "firstName": "Aleksandra", "lastName": "Fedorova" } }

### Transfer

- послать POST запрос с телом { "senderAccountId": (Long), "recipientAccountId": (Long),  "value": (Double) }
  на : localhost:8080/clever/api/transfer
- результатом ожидается новый созданный объект транзакции

### Пример Transfer:

- Отправляем POST запрос с телом { "senderAccountId": 1, "recipientAccountId": 1,  "value": 1500 }
  на : http://localhost:8080/clever/api/transfer
- Ответ сервера: { "id": 7, "amount": 16000.0, "dateTime": "04-09-2023_01:04:26",
  "recipientAccount": { "id": 1, "number": 3273630263392, "amount": 16500.0, "cashbackLastDate": "30-08-2023",
  "bank": { "id": 1, "name": "Clever-Bank" },
  "user": { "id": 1, "firstName": "Aleksandra", "lastName": "Fedorova" } },
  "senderAccount": { "id": 3, "number": 2789246110014, "amount": 268515.0, "cashbackLastDate": "12-12-2022",
  "bank": { "id": 3, "name": "Trust bank" },
  "user": { "id": 3, "firstName": "Adnrey", "lastName": "Parfenov" } } }

### Чеки хранятся в папке tomcat/bin/cheque