# PROJECT KNOWLEDGE BASE

**Generated:** 2026-05-27
**Commit:** 9b645a8
**Branch:** master

## OVERVIEW
Java learning and code runner project. Multi-module Maven (no parent module declaration) with Spring Boot 2.x/3.x, WebFlux/Kotlin, Vert.x, IoT (Netty/MQTT), Flink, and various experimental modules. ~572 files, ~53k lines. Focus: security, distributed locks, code execution, IoT protocols, data processing, LLM integration.

## STRUCTURE
```
./
├── spring-boot-2x/     # Spring Boot 2.7 + Web MVC + Security + MyBatis-Plus (176 files)
├── kotlin-runner/      # Spring Boot 3.2 + WebFlux + Kotlin + Spring AI + R2DBC (169 files)
├── code-runner/        # Spring Boot 3.2 + WebFlux + multi-DB playground + auth (103 files)
├── iot-server/         # Spring Boot 2.7 + Netty + MQTT + multi-server types (36 files)
├── iot-client/         # Spring Boot 2.7 + MQTT + Netty client (13 files)
├── vertx-runner/       # Vert.x 4.5 + Kotlin + coroutines + shade packaging (15 files)
├── iot-net-plugin/     # Netty-only protocol library (6 files)
├── auth/               # EMPTY - only pom.xml (Sa-Token deps placeholder)
├── common/             # EMPTY - only pom.xml (PDFBox/Tess4J deps placeholder)
├── util/               # EMPTY - only pom.xml (JavaCV/Hutool deps placeholder)
├── flink/              # EMPTY - only pom.xml (Flink deps placeholder)
└── h2/                 # H2 database data files (not a Maven module)
```

## WHERE TO LOOK
| Task | Location | Notes |
|------|----------|-------|
| Spring MVC + Security | spring-boot-2x/src/main/java/me/sibyl/ | JWT 0.9, Spring Security, WebSocket |
| WebFlux + Kotlin + AI | kotlin-runner/src/main/kotlin/ | Spring AI Ollama 1.0, R2DBC reactive |
| Code execution + DB | code-runner/src/main/java/code/sibyl/ | WebFlux multi-DB playground (53 files) |
| IoT multi-server | iot-server/src/main/java/org/example/ | BIO/NIO/Netty/TCP/HTTP/Socket/MQTT |
| Vert.x runner | vertx-runner/src/main/kotlin/code/sibyl/ | Vert.x 4.5 + Kotlin + RxJava3 |
| Large file/complexity | spring-boot-2x/.../IndexController.java (1535 lines) | God class, needs refactoring |
| Redis utilities | spring-boot-2x/.../util/RedisUtil.java (1196 lines) | Needs refactoring |
| Auth implementations | spring-boot-2x/.../auth/ | JWT, Login filters |
| Flink integration | kotlin-runner/.../flink/ | Flink 1.20 CDC + Kafka |

## CONVENTIONS
- **Build:** Maven multi-module (root pom is empty parent, no `<modules>` declared)
- **Spring Boot:** 2.7.10 (spring-boot-2x, iot), 3.2.6 (kotlin-runner), 3.2.1 (code-runner), 3.5.4 (empty modules)
- **Java:** 8 (spring-boot-2x, iot), 21 (kotlin-runner, code-runner, vertx-runner) with preview
- **Database:** MyBatis-Plus + Dynamic DataSource + R2DBC reactive + H2 + PostgreSQL + MySQL + SQL Server + Oracle
- **Caching:** Redis (Redisson, Lock4j), Caffeine
- **Security:** Spring Security, JWT (jjwt 0.9.0), Jasypt, Sa-Token (placeholder)
- **Doc/PDF:** iTextPDF 5.5.13, EasyExcel 3.0.5
- **Media:** JavaCV + FFmpeg (video processing)
- **Logging:** Logback rolling (100MB max, 360 history)

## ANTI-PATTERNS (THIS PROJECT)
- **Root pom empty** - no `<modules>` declared, each module builds independently
- **Missing deps** - spring-boot-2x references `sibyl-util`/`sibyl-common` that don't exist
- **Empty modules** - auth/common/util/flink have pom.xml only, no source
- **Duplicate deps** - spring-boot-devtools, tomcat-embed-core, aspectjweaver listed twice
- **AspectJ 1.8.6 is old** - conflicts with Spring Boot 2.7
- **Many commented-out blocks** - code-runner pom.xml, kotlin-runner build plugins
- **6 @Deprecated classes** - R2DBCSourceAOP, R2dbcRoutingConfig, GlobalRestControllerAdvice, FileService, DeepSeekController.chat(), NoRepeatSubmitAroundAspect
- **kotlin-runner auth/v1/ entire directory disabled** - SecurityConfig file 219 lines mostly commented

## UNIQUE STYLES
- **Separate jar packaging** (kotlin-runner): lib/ for deps, external config/ folder
- **Shade uber jar** (vertx-runner): maven-shade-plugin with Main-Verticle manifest
- **Multi-database support:** Both JDBC blocking + R2DBC reactive in same modules
- **Spring AI integration:** Ollama LLM + ONNX Runtime (CLIP)
- **Lock4j + Redisson:** Distributed lock patterns
- **Dynamic DataSource (Baomidou):** Multi-database routing
- **IoT server farm:** 6 server types (BIO/NIO/Netty-TCP/HTTP/Socket/MQTT) via strategy pattern

## COMMANDS
```bash
# Build specific module
cd spring-boot-2x && mvn clean package -DskipTests

# Run spring-boot-2x
cd spring-boot-2x && mvn spring-boot:run

# Build kotlin-runner (custom packaging)
cd kotlin-runner && mvn clean package -DskipTests
java -jar target/kotlin-runner/kotlin-runner-0.0.1-SNAPSHOT.jar

# Build code-runner
cd code-runner && mvn clean package -DskipTests

# Build vertx-runner
cd vertx-runner && mvn clean package -DskipTests

# Build iot-server
cd iot-server && mvn clean package
```

## NOTES
- IndexController.java (1535 lines) and RedisUtil.java (1196 lines) need refactoring
- All modules build independently - no root `mvn install` (missing `<modules>`)
- Tests exist in spring-boot-2x, kotlin-runner, code-runner (8 files total) - all integration-only, zero assertions
- kotlin-runner tests are SKIPPED by default (`<skip>true</skip>` in surefire)
- Package naming inconsistent: `me.sibyl` / `code.sibyl` / `org.example`
- h2/ directory is H2 database storage, not source code
