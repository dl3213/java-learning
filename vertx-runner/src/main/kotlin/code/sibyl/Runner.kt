package code.sibyl

import io.vertx.core.AbstractVerticle
import io.vertx.core.DeploymentOptions
import io.vertx.core.Launcher
import io.vertx.core.ThreadingModel

class Runner : AbstractVerticle() {

    override fun start() {
        vertx.deployVerticle(
            Server::class.java.getName(),
            DeploymentOptions()
                .setThreadingModel(ThreadingModel.WORKER)
                .setWorkerPoolName("code-sibyl-vertx-runner")
                .setWorkerPoolSize(32)
                .setMaxWorkerExecuteTime(3 * 60L * 1000 * 1000000)
        )
    }

    override fun stop() {
        super.stop()
        println("runner stopped...")
    }
}

fun main() {
    Launcher.executeCommand("run", Runner::class.java.getName())
}