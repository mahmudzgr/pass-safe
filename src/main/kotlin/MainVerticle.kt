import io.vertx.core.AbstractVerticle
import io.vertx.core.http.HttpMethod

class MainVerticle : AbstractVerticle() {

    val publicPath = "public"

    override fun start() {
        val server = vertx.createHttpServer()
        server.requestHandler { request ->
            if (request.method() != HttpMethod.GET) {
                request.response().setStatusCode(404).end("404 Not Found")
                return@requestHandler
            }
            val filePath = publicPath + if (request.uri() == "/") "/index.html" else request.uri()
            println(filePath)
            if (vertx.fileSystem().existsBlocking(filePath)) {
                request.response().sendFile(filePath)
            } else {
                request.response().setStatusCode(404).end("404 Not Found")
                return@requestHandler
            }
        }
        server.listen(8080) { res ->
            if (res.succeeded()) {
                println("Server is now listening...")
            } else {
                println("Failed to bind.")
            }
        }
    }

    override fun stop() {
        println("MainVerticle is stopped.")
    }
}