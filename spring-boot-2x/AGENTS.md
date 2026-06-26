# MODULE KNOWLEDGE BASE: spring-boot-2x

**Generated:** 2026-05-27
**Commit:** 9b645a8
**Branch:** master

## OVERVIEW
Spring Boot 2.7.10 Web MVC application with Spring Security, MyBatis-Plus, JWT authentication, dynamic data sources, Redis caching, WebSocket, and file processing capabilities.

## STRUCTURE
```
spring-boot-2x/src/main/java/me/sibyl/
├── controller/          # REST controllers (20 files, IndexController 1535 lines)
├── service/             # Business logic
├── service/impl/        # Implementations
├── auth/                # JWT, login filters, security config
├── config/              # Spring configs
├── util/                # Utilities
├── aspect/              # AOP aspects
└── listener/            # Event listeners
```

## KEY FEATURES
- **Security:** Spring Security + JWT (jjwt 0.9.0) + Jasypt encryption
- **Database:** MyBatis-Plus, Dynamic DataSource, Druid, PostgreSQL
- **Caching:** Redis + Caffeine
- **File Processing:** iTextPDF, EasyExcel, JavaCV/FFmpeg
- **Real-time:** WebSocket
- **Locks:** Lock4j + Redisson distributed locks

## HOTSPOTS
| File | Lines | Concern |
|------|-------|---------|
| IndexController.java | 1535 | Needs refactoring, God class |
| FileController.java | 694 | File upload/download logic |
| IndexRestController.java | 563 | REST API endpoints |

## CONVENTIONS
- Java 8
- Controller layer thick (business logic in controllers instead of services) - anti-pattern but consistent
- Dynamic DataSource for multi-db routing
- MyBatis-Plus for CRUD operations

## ANTI-PATTERNS
- Business logic in controllers instead of service layer
- Very large controller files (1500+ lines)
- Duplicate pom.xml dependencies (aspectjweaver, spring-boot-devtools, tomcat-embed-core)

## COMMANDS
```bash
cd spring-boot-2x
mvn clean package -DskipTests
mvn spring-boot:run
```
