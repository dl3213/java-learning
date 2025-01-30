package code.sibyl

import code.sibyl.common.r
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
    log.info("JVM available processors: ${runtime.availableProcessors()}")
    log.info("JVM free memory: ${runtime.freeMemory() / 1024 / 1024} MB")
    log.info("JVM total memory: ${runtime.totalMemory() / 1024 / 1024} MB")
    log.info("JVM max memory: ${runtime.maxMemory() / 1024 / 1024} MB")
    log.info("JVM version: ${System.getProperty("java.version")}")
    log.info("JVM vendor: ${System.getProperty("java.vendor")}")
    log.info("JVM class path: ${System.getProperty("java.class.path")}")
    log.info("JVM home: ${System.getProperty("java.home")}")
    log.info("JVM spec version: ${System.getProperty("java.specification.version")}")
    log.info("JVM spec vendor: ${System.getProperty("java.specification.vendor")}")
    log.info("JVM spec name: ${System.getProperty("java.specification.name")}")

}
