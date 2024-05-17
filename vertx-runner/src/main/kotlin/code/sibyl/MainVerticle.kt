package code.sibyl

import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.common.template.TemplateEngine
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.ext.web.handler.TemplateHandler
import io.vertx.ext.web.templ.thymeleaf.ThymeleafTemplateEngine


class MainVerticle : AbstractVerticle() {

    override fun start(startPromise: Promise<Void>) {

        val engine: TemplateEngine? = ThymeleafTemplateEngine.create(vertx)
        val handler: TemplateHandler = TemplateHandler.create(engine)

        val router: Router = Router.router(vertx)
        router.route("/dist/*").handler(StaticHandler.create("static/dist"));
        router.route("/css/*").handler(StaticHandler.create("static/css"));
        router.route("/static/*").handler(StaticHandler.create("static/static"));

        router.get("/").handler(handler);
        router.getWithRegex(".+\\.html").handler(handler);

        router["/"]
                .handler { ctx: RoutingContext -> ctx.reroute("/index.html") }
        router["/index"]
                .handler { ctx: RoutingContext -> ctx.reroute("/index.html") }

        vertx
                .createHttpServer()
                .requestHandler(router)
                .listen(8888) { http ->
                    if (http.succeeded()) {
                        startPromise.complete()
                        println("HTTP server started on port 8888")
                    } else {
                        startPromise.fail(http.cause());
                    }
                }
    }
}