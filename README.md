# Bite Shore
Bite Shore

Backend-застосунок на Java та Spring Boot, який отримує погодні дані, зберігає їх, 
аналізує погодні умови та формує рекомендації щодо риболовлі.

Technologies
Java 21
Spring Boot 3.5.5
Spring Data JPA
PostgreSQL 17
Maven
Lombok
MapStruct
WebClient
JUnit 5
Mockito
Jackson
Docker
Docker Compose
Architecture
Client
↓
REST Controller
↓
FishingAnalysisService
↓
WeatherOrchestratorService
↓
Weather API
↓
Weather Storage
↓
WeatherHistoryService
↓
Aggregators
↓
Analyzers
↓
GeneralAnalyzer
↓
FishingAnalysisResponse
Main Components
Weather

WeatherAPIClient отримує погодні дані із зовнішнього Weather API.

WeatherService обробляє отримані погодні дані.

WeatherMapper перетворює дані зовнішнього API у внутрішні об'єкти застосунку.

WeatherOrchestratorService керує процесом отримання та збереження погодних даних.

WeatherStorageService відповідає за збереження погодних даних у базі даних.

WeatherHistoryService завантажує збережені погодні дані для подальшого аналізу.

Aggregators

Агрегатори об'єднують погодні дані та формують значення, необхідні для подальшого аналізу.

Основні агрегатори:

TemperatureAggregator
PressureAggregator
WindAggregator
PrecipitationAggregator
Analyzers

Аналізатори оцінюють окремі погодні параметри та переводять отримані значення у відповідні стани.

Основні аналізатори:

TemperatureAnalyzer
PressureAnalyzer
WindAnalyzer
PrecipitationAnalyser
GeneralAnalyzer

GeneralAnalyzer отримує результати окремих аналізаторів та на їх основі формує загальну рекомендацію 
щодо риболовлі.

TemperatureAnalyzer
↓
PressureAnalyzer
↓
WindAnalyzer
↓
PrecipitationAnalyser
↓
GeneralAnalyzer
↓
FishingAnalysisResponse
Weather Analysis

Застосунок аналізує основні погодні фактори:

температуру повітря;
атмосферний тиск;
вітер;
опади;
температуру води;
зміни температури;
сезонні умови.

На основі цих параметрів визначаються стани погодних умов та формується рекомендація для риболовлі.

Fishing Analysis

Для аналізу користувач передає:

широту;
довготу;
місто;
дату риболовлі.

Для отримання погодних даних можна використовувати координати або місто.

Основний процес:

FishingAnalysisService
↓
Координати або місто
↓
WeatherOrchestratorService
↓
Weather API
↓
Збереження погодних даних
↓
WeatherHistoryService
↓
Aggregators
↓
Analyzers
↓
GeneralAnalyzer
↓
Рекомендація
Water Temperature

Температура води розраховується на основі погодних даних та її попереднього значення.

Якщо попереднє значення температури води відсутнє, використовується початкова оцінка на основі
середньої температури повітря.

Для наступних розрахунків використовується попереднє значення температури води та 
поточна температура повітря.

Температура води також використовується для оцінки температурних умов для риболовлі.

Pressure Analysis

Атмосферний тиск оцінюється відносно заданого базового значення.

Поточна логіка використовує такі стани:

< 752       → LOW
752–758     → NORMAL
> 758       → HIGH

Також враховується зміна атмосферного тиску відносно попередніх значень.

Wind Analysis

Для оцінки впливу вітру враховуються:

швидкість вітру;
пориви вітру;
наближеність дати прогнозу до дати риболовлі.

При оцінці використовується комбінований показник швидкості вітру та поривів.

Чим далі дата прогнозу від поточного дня, тим більший запас враховується під час оцінки умов.

Precipitation Analysis

Опади поділяються на кілька станів залежно від їх інтенсивності:

NONE
LIGHT
MODERATE
HEAVY

Інтенсивність опадів також враховується під час формування загальної рекомендації.

Опади можуть додатково впливати на оцінку температурних умов.

Temperature Analysis

Аналіз температури враховує:

денну температуру;
нічну температуру;
середню температуру;
температуру попередніх днів;
температурний тренд.

Якщо нічна температура відсутня, для розрахунку використовується доступна денна температура.

Результат аналізу переводиться у відповідний стан температурних умов.

Database

Для зберігання погодних даних використовується PostgreSQL.

Основні параметри:

Database: PostgreSQL
Port: 5432
Database name: bite_short

Для роботи з базою даних використовується Spring Data JPA та Hibernate.

Поточна стратегія автоматичного оновлення структури бази:

spring.jpa.hibernate.ddl-auto=update

Основні таблиці:

weather_data
weather_location
REST API

Основний endpoint для аналізу риболовлі:

POST /api/fishing/analyze
Request
{
"latitude": 48.4647,
"longitude": 35.0462,
"city": "Дніпро",
"fishingDate": "2026-08-05"
}
Response
{
"fishingDate": "2026-08-05",
"recommendation": "..."
}
Validation

Дата риболовлі не може бути в минулому.

Для визначення місця прогнозу можуть використовуватися координати або назва міста.

Testing

Для тестування використовуються:

JUnit 5
Mockito
Spring Boot Test

У проєкті є unit-тести для основних аналізаторів:

PressureAnalyzerTest
TemperatureAnalyzerTest
WindAnalyzerTest
PrecipitationAnalyserTest

Також використовуються інтеграційні тести для перевірки роботи застосунку разом зі Spring-контекстом,
базою даних та зовнішніми компонентами.

Project Structure
src
├── main
│   ├── java
│   │   └── ...
│   └── resources
│       └── application.properties
│
└── test
└── java
└── ...

Основна логіка проєкту розділена на:

Controller
↓
Service
↓
Orchestrator
↓
API / Storage
↓
History
↓
Aggregators
↓
Analyzers
↓
GeneralAnalyzer
Environment Variables

Для доступу до зовнішнього Weather API використовується змінна середовища:

API_KEY

При запуску через Docker Compose значення передається через файл .env.

Приклад:

API_KEY=your_api_key

Файл .env не повинен публікуватися у Git-репозиторії.

Docker

Застосунок контейнеризований за допомогою Docker.

Docker image застосунку:

student78/bite-shore:1.0

Image опублікований у Docker Hub.

Docker Hub — student78/bite-shore

Docker Compose

Для запуску застосунку разом із PostgreSQL використовується Docker Compose.

Архітектура:

Docker Compose
│
├── app
│   └── Bite Shore
│
└── db
└── PostgreSQL 17

Застосунок підключається до PostgreSQL через ім'я сервісу Docker Compose:

jdbc:postgresql://db:5432/bite_short

Для збереження даних PostgreSQL використовується Docker Volume:

postgres_data

Це дозволяє зберігати дані бази після перезапуску або пересоздання контейнерів.

Running Locally

Для локального запуску без Docker необхідно:

Встановити Java 21.
Встановити Maven.
Запустити PostgreSQL.
Створити базу даних bite_short.
Налаштувати параметри підключення до бази даних.
Встановити API_KEY.
Запустити Spring Boot застосунок.

Застосунок запускається на порту:

8080
Running with Docker Compose

Для запуску через Docker необхідно мати:

Docker Desktop;
compose.yml;
.env.

Запуск:

docker compose up -d

Перевірка контейнерів:

docker compose ps

Перегляд логів застосунку:

docker compose logs -f app

Зупинка:

docker compose down

При використанні docker compose down без параметра -v Docker Volume PostgreSQL не видаляється,
тому дані бази зберігаються.

Docker Hub Image

Готовий Docker image можна отримати з Docker Hub:

docker pull student78/bite-shore:1.0

Для запуску на іншому комп'ютері не потрібно переносити вихідний Java-код або Dockerfile,
якщо використовується готовий image з Docker Hub.

Project Goal

Мета проєкту — створити backend-систему, яка:

отримує погодні дані;
зберігає їх у PostgreSQL;
формує історію погодних даних;
агрегує отримані дані;
аналізує основні погодні фактори;
визначає погодні умови;
формує рекомендацію щодо риболовлі.

Проєкт також використовується для практичного вивчення Java, Spring Boot, PostgreSQL, REST API,
тестування та Docker.
