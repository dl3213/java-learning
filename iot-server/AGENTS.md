# 模块知识库: iot-server

**Generated:** 2026-05-27
**Commit:** 9b645a8
**Branch:** master

## OVERVIEW
Spring Boot 2.7.14 + Netty 4.1.95 + MQTT IoT 服务器，支持 6 种服务器类型通过策略模式启动。Java 8。

## STRUCTURE
```
src/main/java/org/example/
├── IotServer.java                    # 主入口, WebApplicationType.NONE
├── config/                           # 异步线程池配置
├── codec/                            # 编解码
├── mqtt/
│   ├── MessageStrategyManager.java   # MQTT 消息策略分发
│   └── message/strategy/             # 7 种 MQTT 消息策略
├── runner/                           # 5 个 CommandLineRunner 分别启动各服务器
│   ├── BioServerStartRunner.java
│   ├── HttpServerStartRunner.java
│   ├── MqttServerStartRunner.java
│   ├── SocketServerStartRunner.java
│   └── TcpServerStartRunner.java
├── server/
│   ├── bio/BioServer.java            # 传统 BIO Socket
│   ├── http/HttpServer.java          # Netty HTTP
│   ├── mqtt/MqttBroker.java          # Netty MQTT Broker
│   ├── nio/NioServer.java            # Java NIO
│   ├── socket/SocketServer.java      # Netty Socket
│   └── tcp/TcpServer.java            # Netty TCP
└── util/SpringUtil.java
```

## WHERE TO LOOK
| 任务 | 位置 |
|------|------|
| 新增服务器类型 | `server/` + `runner/` (实现 Server 接口 + 注册 Runner) |
| MQTT 消息处理 | `mqtt/message/strategy/`, 实现 MessageStrategy 接口 |
| MQTT 策略分发 | `mqtt/MessageStrategyManager.java` |
| 协议编解码 | `codec/` |

## CONVENTIONS
- **Java 8**, Spring Boot 2.7.14
- **包名:** `org.example` (模板值未修改)
- **启动:** 无 Web 容器 (`WebApplicationType.NONE`)，纯后台服务
- **架构:** 每个服务器类型独立实现，通过 Runner 并行启动
- **MQTT:** 策略模式处理消息类型 (CONNECT/PUBLISH/SUBSCRIBE/PING 等)
- **构建:** `mvn clean package`，无 `spring-boot-maven-plugin` 配置，无 Dockerfile
- **依赖:** iot-net-plugin (Netty 协议库), Protobuf

## ANTI-PATTERNS
- **包名 `org.example`** — Maven archetype 默认包名未修改，需迁移到 `code.sibyl.iot`
- **无 spring-boot-maven-plugin** — 打包后不可直接 `java -jar` 运行
- **无 Dockerfile** — 无法容器化部署
- **lombok scope=compile** — 应改为 `provided` 避免打入 jar
- **BIO 服务器** — 生产环境不应使用 BIO，仅作教学演示

## COMMANDS
```bash
cd iot-server && mvn clean package
java -cp target/iot-server-1.0-SNAPSHOT.jar;target/lib/* org.example.IotServer
```
