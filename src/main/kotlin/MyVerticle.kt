import io.vertx.core.AbstractVerticle

class MyVerticle : AbstractVerticle() {

    override fun start() {
        println("Started")
        vertx.setTimer(5000) {
            vertx.close()
        }
    }

    override fun stop() {
        println("Stopped")
    }

}