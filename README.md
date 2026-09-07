# Bite Shore
Bite Shore

Backend-застосунок на Java та Spring Boot, який аналізує погодні умови та формує рекомендації щодо риболовлі.

Technologies
Java 17
Spring Boot 3.5.5
Spring Data JPA
PostgreSQL
Maven
Lombok
MapStruct
WebClient
JUnit 5
Mockito
Jackson
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

WeatherService обробляє отримані дані.

WeatherMapper перетворює дані API у внутрішні об'єкти застосунку.

WeatherOrchestratorService керує отриманням і збереженням погодних даних.

WeatherStorageService відповідає за збереження погодних даних.

WeatherHistoryService завантажує збережені погодні дані для подальшого аналізу.

Aggregators

Агрегатори об'єднують погодні дані та формують добові значення.

TemperatureAggregator
PressureAggregator
WindAggregator
PrecipitationAggregator
Analyzers

Аналізатори оцінюють окремі погодні параметри.

TemperatureAnalyzer
PressureAnalyzer
WindAnalyzer
PrecipitationAnalyser
GeneralAnalyzer

GeneralAnalyzer отримує результати окремих аналізаторів та на їх основі формує загальну рекомендацію щодо риболовлі.

Weather Analysis

Застосунок аналізує основні погодні фактори:

температуру;
атмосферний тиск;
вітер;
опади;
зміни температури;
сезонні умови.

На основі цих параметрів визначається стан погодних умов та формується рекомендація для риболовлі.

Fishing Analysis

Для аналізу користувач передає:

широту;
довготу;
місто;
дату риболовлі.

Можна використовувати або координати, або місто для отримання погодних даних.

FishingAnalysisService
↓
Координати або місто
↓
WeatherOrchestratorService
↓
Weather API
↓
Збереження даних
↓
Aggregators
↓
Analyzers
↓
GeneralAnalyzer
↓
Рекомендація
Database

Для зберігання погодних даних використовується PostgreSQL.

Основні налаштування:

Database: PostgreSQL
Port: 5432
Database name: bite_short

Hibernate використовується для роботи з базою даних через Spring Data JPA.

spring.jpa.hibernate.ddl-auto=update
REST API

Основний endpoint для аналізу риболовлі:

POST /api/fishing/analyze

Приклад запиту:

{
"latitude": 48.4647,
"longitude": 35.0462,
"city": "Дніпро",
"fishingDate": "2026-08-05"
}

Приклад відповіді:

{
"fishingDate": "2026-08-05",
"recommendation": "..."
}
Testing

Для тестування використовуються:

JUnit 5
Mockito
Spring Boot Test

У проєкті є unit-тести для основних аналізаторів:

PressureAnalyzerTest
TemperatureAnalyzerTest
WindAnalyzerTest

Також використовуються інтеграційні тести для перевірки роботи застосунку разом зі Spring-контекстом, базою даних та зовнішніми компонентами.

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
Running the Application

Для запуску застосунку необхідно:

Встановити Java 17.
Встановити Maven.
Запустити PostgreSQL.
Налаштувати параметри підключення до бази даних.
Запустити Spring Boot застосунок.

Застосунок запускається на порту:

8080
Project Goal

Мета проєкту — створити backend-систему, яка отримує погодні дані, зберігає їх, аналізує основні погодні фактори та формує рекомендації щодо доцільності риболовлі.

Future Improvements

Планується подальший розвиток проєкту:

покращення алгоритмів аналізу;
розширення системи рекомендацій;
збільшення кількості тестів;
покращення обробки погодних даних;
додавання нових факторів, які впливають на активність риби;
контейнеризація застосунку за допомогою Docker.
