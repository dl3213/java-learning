# kotlin-runner 运行镜像（thin）
# 前置：先在本机完成 Maven 构建，产出 target/kotlin-runner/{jar, lib/, config/}
#   for m in common util flink auth; do (cd $m && mvn install -DskipTests -q); done
#   cd kotlin-runner && mvn clean package -DskipTests
FROM eclipse-temurin:21-jre

WORKDIR /app

# 整体拷贝构建产物：jar + lib/ + config/
# （manifest 的 Class-Path 依赖该目录结构，config/ 会被运行时挂载覆盖）
COPY target/kotlin-runner/ .

ENV TZ=Asia/Shanghai \
    JAVA_OPTS="-Xms256m -Xmx256m -XX:+HeapDumpOnOutOfMemoryError"

# sh -c + exec：SIGTERM 直达 java 进程，配合 server.shutdown: graceful 实现优雅停机
# 编译启用了 preview 特性，运行必须带 --enable-preview 且 JDK 大版本为 21
ENTRYPOINT ["sh","-c","exec java $JAVA_OPTS --enable-preview -jar kotlin-runner-0.0.1-SNAPSHOT.jar"]
