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
                .setThreadingModel(ThreadingModel.VIRTUAL_THREAD)
                .setWorkerPoolName("code-sibyl-vertx-runner")
                .setMaxWorkerExecuteTime(3 * 60L * 1000 * 1000000)
        )
    }
}

fun main() {
    Launcher.executeCommand("run", Runner::class.java.getName())
}