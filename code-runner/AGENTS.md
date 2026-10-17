# MODULE KNOWLEDGE BASE: code-runner

**Generated:** 2026-05-27
**Commit:** 9b645a8
**Branch:** master

## OVERVIEW
Spring Boot 3.2.1 + WebFlux 多数据库管理控制台，支持 H2/PostgreSQL/MySQL/SQL Server/Oracle 双驱动 (JDBC + R2DBC)

## STRUCTURE
```
src/main/java/code/sibyl/
├── Application.java
├── aop/SqlLoggingAop.java
├── auth/                           # 双认证: REST (前后端分离) + sys (传统)
│   ├── SecurityConfig.java         # @EnableWebFluxSecurity, 双 SecurityWebFilterChain
│   ├── LoginFilter.java            # kaptcha 验证码
│   ├── rest/                       # RestAuthManager + handler/entrypoint
│   └── sys/                        # SysAuthenticationManager (Servlet 风格) + Reactive
├── common/                         # Response, Either, DataBaseTypeEnum
├── config/                         # DatabaseConfiguration, DBInitConfig, SysConfig
├── controller/
│   ├── IndexController.java        # 首页/菜单 (89行, Mono<String> + Model)
│   ├── database/                   # 数据库管理 CRUD (Controller + Handler + Router)
│   ├── rest/                       # REST 控制器
│   └── sys/user/
├── domain/ + dto/                  # 实体 + 传输对象
├── filter/                         # Reactive 请求上下文
├── repository/                     # DatabaseRepository, SysUserRepository
├── router/RouterConfig.java        # WebFlux 函数式路由
├── runner/ + service/              # SystemRunner, DataBaseService, SysUserService
└── util/                           # UserUtil, SpringUtil, TreeUtil, r.java

src/main/resources/
├── application.yml                 # 端口 10001, H2 文件库, Thymeleaf, devtools
├── static/                         # Layui + Bootstrap 5 + ECharts + Axios
└── templates/                      # Thymeleaf: dashboard, login, 数据库/用户管理
    database/{add-view,list-view}.html
    sys/user/{add-view,list-view}.html
```

## WHERE TO LOOK

| 任务 | 位置 |
|------|------|
| 数据库管理控制台 | `controller/database/` + `templates/database/` |
| Security 双链配置 | `auth/SecurityConfig.java` |
| 传统认证 | `auth/sys/SysAuthenticationManager.java` + `LoginFilter` |
| REST 认证 | `auth/rest/RestAuthManager.java` |
| 动态数据库路由 | `service/DataBaseService.java` |
| 函数式路由 | `router/RouterConfig.java` |

## CONVENTIONS

- **Java 21**, Spring Boot 3.2.1, WebFlux (Netty 非 Tomcat)
- **包名:** `code.sibyl` (kotlin-runner 同，spring-boot-2x 用 `me.sibyl`)
- **数据库:** H2 文件库默认 + 多数据源代码动态注册 (JDBC + R2DBC 双驱)
- **认证:** `/api/rest/**` 走 `@Order(0)` REST 链, 其余走 `@Order(1)` 传统链 + kaptcha
- **视图:** Thymeleaf + Layui 管理后台，非纯 API
- **构建:** `mvn clean package -DskipTests`，`<build>` 全注释，用默认 spring-boot-maven-plugin

## ANTI-PATTERNS

- **`<build>` 全部注释** — pom.xml 中 100+ 行 lib/config 分离打包配置被注释
- **`spring.security.strategy: MODE_INHERITABLETHREADLOCAL`** — WebFlux 反应式环境应使用 Reactor Context
- **`tomcat.connection-timeout`** — WebFlux 默认 Netty，Tomcat 配置不生效
- **`NoPasswordEncoder`** — 密码可能明文存储，无 BCrypt 等编码
- **`SqlLoggingAop`** — AOP 切面存在但可能未配置切入点，实际未生效
