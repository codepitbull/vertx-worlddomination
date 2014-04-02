package de.codepitbull.worlddomination.mcp.controller

import org.junit.Test
import org.vertx.java.core.AsyncResult
import org.vertx.java.core.Handler
import org.vertx.java.core.eventbus.Message
import org.vertx.testtools.TestVerticle

import static org.vertx.testtools.VertxAssert.*

/**
 *
 * @author Jochen Mader
 */
class ControllerVerticleTest extends TestVerticle{

    @Test
    public void testConnect() {
        vertx.eventBus().registerHandler("robots.announce", new Handler<Message>() {
            @Override
            void handle(Message event) {
                assertEquals("robby", event.body())
                testComplete()
            }
        })

        container.deployVerticle("groovy:"+ControllerVerticle.class.getName(), new Handler<AsyncResult<String>>() {
            @Override
            void handle(AsyncResult<String> event) {
                Socket socket = new Socket()
                socket.connect(new InetSocketAddress("localhost", 2000), 50)
                socket.getOutputStream().write("hello:robby,0,0,n\n".getBytes())
                socket.getOutputStream().flush()
                socket.close()
            }
        });
    }
}
