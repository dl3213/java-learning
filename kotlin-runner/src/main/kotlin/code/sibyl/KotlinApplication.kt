package code.sibyl

import code.sibyl.common.r
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.env.Environment

@SpringBootApplication
class KotlinApplication

private val log = LoggerFactory.getLogger(KotlinApplication::class.java)

fun main(args: Array<String>) {
    runApplication<KotlinApplication>(*args)
    log.info("app running in port = " + r.getBean(Environment::class.java).getProperty("server.port"))
    log.info("app running in env = " + r.env())
    log.info("app running in path = " + r.absolutePath())
    log.info("app running in fileBaseDir = " + r.fileBaseDir())
//    System.getProperties().list(System.out)
    val runtime = Runtime.getRuntime()
    println("JVM available processors: ${runtime.availableProcessors()}")
    println("JVM free memory: ${runtime.freeMemory() / 1024 / 1024} MB")
    println("JVM total memory: ${runtime.totalMemory() / 1024 / 1024} MB")
    println("JVM max memory: ${runtime.maxMemory() / 1024 / 1024} MB")
    println("JVM version: ${System.getProperty("java.version")}")
    println("JVM vendor: ${System.getProperty("java.vendor")}")
    println("JVM class path: ${System.getProperty("java.class.path")}")
    println("JVM home: ${System.getProperty("java.home")}")
    println("JVM spec version: ${System.getProperty("java.specification.version")}")
    println("JVM spec vendor: ${System.getProperty("java.specification.vendor")}")
    println("JVM spec name: ${System.getProperty("java.specification.name")}")

}
