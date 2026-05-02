# PROJECT KNOWLEDGE BASE

**Generated:** 2026-04-30 23:12:39
**Commit:** 826192e
**Branch:** master

## OVERVIEW
Java learning and code runner project. Multi-module Maven project with Spring Boot 2.x, WebFlux/Kotlin, Flink, and various experimental modules. Focus: security, distributed locks, code execution, IoT, data processing.

## STRUCTURE
```
./
├── spring-boot-2x/     # Spring Boot 2.7 + Web MVC + Security + MyBatis-Plus
├── kotlin-runner/      # Spring Boot 3.2 + WebFlux + Kotlin + Spring AI + R2DBC
├── sibyl-util/         # Shared utilities
├── code-runner/        # Code execution service
├── auth/               # Authentication module
├── common/             # Common shared module
├── util/               # Additional utilities
├── flink/              # Apache Flink processing
├── iot-server/         # IoT server
├── iot-client/         # IoT client
├── iot-net-plugin/     # IoT network plugin
└── vertx-runner/       # Vert.x runner
```

## WHERE TO LOOK
| Task | Location | Notes |
|------|----------|-------|
| Spring MVC + Security | spring-boot-2x/src/main/java/me/sibyl/ | JWT, Spring Security, WebSocket |
| WebFlux + Kotlin + AI | kotlin-runner/src/main/kotlin/ | Spring AI Ollama, R2DBC, Redis Reactive |
| Large file/complexity | spring-boot-2x/src/main/java/me/sibyl/controller/IndexController.java (1535 lines) |
| Redis utilities | sibyl-util/src/main/java/me/sibyl/structure/util/RedisUtil.java (1196 lines) |
| Code execution | code-runner/ | Code runner service |
| Auth implementations | spring-boot-2x/src/main/java/me/sibyl/auth/ | JWT, Login filters |

## CONVENTIONS
- **Build:** Maven multi-module, root pom.xml is parent pom (packaging: pom)
- **Spring Boot versions:** 2.7.10 (stable), 3.2.6 (Kotlin/WebFlux)
- **Java:** 8 (spring-boot-2x), 21 (kotlin-runner) with preview
- **Database tech:** MyBatis-Plus, Dynamic DataSource, R2DBC (reactive), H2, PostgreSQL, MySQL, SQL Server, Oracle
- **Caching:** Redis (Redisson, Lock4j), Caffeine
- **Security:** Spring Security, JWT (jjwt 0.9.0), Jasypt
- **Doc/PDF:** iTextPDF 5.5.13, EasyExcel 3.0.5
- **Media:** JavaCV + FFmpeg (video processing)

## ANTI-PATTERNS (THIS PROJECT)
- Duplicate dependencies in pom.xml (spring-boot-devtools, tomcat-embed-core listed twice)
- AspectJ version 1.8.6 is old; conflicts likely with Spring Boot 2.7
- Many commented-out dependency blocks - keep cleanup
- Build plugins in kotlin-runner have large commented sections

## UNIQUE STYLES
- **Custom jar packaging in kotlin-runner:** lib/ dir for dependencies, external config/ folder
- **Multi-database support:** Both blocking (JDBC) and reactive (R2DBC) in same module
- **Spring AI integration:** Ollama for LLM capabilities
- **Lock4j + Redisson:** Distributed lock patterns
- **Dynamic DataSource (Baomidou):** Multi-database routing

## COMMANDS
```bash
# Build all modules
mvn clean install -DskipTests

# Build specific module
cd spring-boot-2x && mvn clean package

# Run kotlin-runner (custom build)
cd kotlin-runner && mvn package
java -jar target/kotlin-runner/kotlin-runner-0.0.1-SNAPSHOT.jar
```

## NOTES
- IndexController.java (1535 lines) needs refactoring
- RedisUtil.java (1196 lines) needs refactoring
- Many modules share pom.xml patterns - consider root dependencyManagement
- No existing tests structure visible in the scanned files
