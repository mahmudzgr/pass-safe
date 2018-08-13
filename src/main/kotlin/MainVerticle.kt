import io.vertx.core.AbstractVerticle
import io.vertx.core.Handler
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.StaticHandler

class MainVerticle : AbstractVerticle() {

    val staticPath = "public/"

    override fun start() {
        val port = (System.getenv("PORT") ?: "8080").toInt()
        val server = vertx.createHttpServer()
        val router = Router.router(vertx)

        val apiRouter = createApiRouter()
        router.mountSubRouter("/api", apiRouter)

        val staticRoute = router.route("/*").handler(StaticHandler.create(staticPath))

        server.requestHandler(router::accept)
        server.listen(port)
        println("Server is now listening on port $port...")
    }

    override fun stop() {
        println("MainVerticle is stopped.")
    }

    private fun createApiRouter(): Router {
        val apiRouter = Router.router(vertx)
        apiRouter.get("/generate/:length").handler(generatePassword)
        return apiRouter
    }

    val generatePassword = Handler<RoutingContext> { context ->
        val request = context.request()
        val response = context.response()
        response.putHeader("Content-Type", "application/json")

        var success = false
        var password = ""
        val result = JsonObject()

        try {
            val length = request.getParam("length").toInt()
            for (i in 1..length) {
                password += (65 + (Math.random() * (122 - 65 + 1)).toInt()).toChar()
            }
            success = true
        } catch (e: Exception) {
            println("Couldn't generate password.\nMessage: ${e.message}")
        } finally {
            result.put("success", success)
            result.put("password", password)
        }
        response.end(Json.encode(result))
    }
}