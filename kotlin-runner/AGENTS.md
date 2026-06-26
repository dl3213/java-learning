# MODULE KNOWLEDGE BASE: kotlin-runner

**Generated:** 2026-05-27
**Commit:** 9b645a8
**Branch:** master

## OVERVIEW
Spring Boot 3.2.6 WebFlux application with Kotlin, R2DBC reactive database, Spring AI (Ollama), Redis Reactive, and Flink integration. Custom jar packaging with external configs and lib directory.

## STRUCTURE
```
kotlin-runner/src/main/kotlin/code/sibyl/
├── controller/          # REST controllers
├── controller/rest/     # REST API endpoints (13 files)
├── service/             # Business logic (12 files)
├── common/              # Common utilities (10 files)
├── auth/v1/sys/         # Authentication (7 files)
└── config/              # Spring configs
```

## KEY FEATURES
- **Web:** Spring WebFlux (reactive)
- **Language:** Kotlin 2.0.0, Java 21 with preview
- **Database:** R2DBC (H2, MySQL, PostgreSQL, Oracle, SQL Server) + JPA
- **AI:** Spring AI Ollama 1.0.0-M5, ONNX Runtime for CLIP
- **Caching:** Redis Reactive + Redisson + Caffeine + Lock4j
- **Data:** Apache Flink 1.20.0 integration
- **Packaging:** Custom Maven build with lib/ for dependencies, external config/ directory

## BUILD SYSTEM
- **Custom packaging:** Dependencies copied to lib/, config files external
- **Main class:** code.sibyl.KotlinApplicationKt
- **Tests skipped by default** in surefire plugin
- **Sources jar generated** for debugging

## DEPENDENCIES
Multi-module dependencies: common, util, flink, auth

## CONVENTIONS
- Reactive programming with Project Reactor
- Kotlin coroutines support
- R2DBC for reactive database access
- External configuration files (not packaged in jar)

## ANTI-PATTERNS
- Large commented build plugin sections
- R2DBC connection pool dependency commented out
- HttpClient5 and OkHttp dependencies commented

## COMMANDS
```bash
cd kotlin-runner
mvn clean package -DskipTests
java -jar target/kotlin-runner/kotlin-runner-0.0.1-SNAPSHOT.jar
```

## NOTES
- Java 21 preview features enabled
- Multiple Maven repositories configured (Maven Central, Aliyun, 4thline, Spring Milestones)
