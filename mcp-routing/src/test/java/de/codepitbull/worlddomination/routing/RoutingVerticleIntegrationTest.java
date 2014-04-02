package de.codepitbull.worlddomination.routing;

import org.junit.Test;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.testtools.TestVerticle;


import static org.vertx.testtools.VertxAssert.*;
import static org.vertx.testtools.VertxAssert.assertTrue;

public class RoutingVerticleIntegrationTest extends TestVerticle {

    @Test
    public void testRoutingFromTo() {
        container.logger().info("in testRoutingFromTo()");
        JsonObject jsonObject = new JsonObject();
        jsonObject.putNumber("from_x", 1);
        jsonObject.putNumber("from_y", 1);
        jsonObject.putNumber("to_x", 9);
        jsonObject.putNumber("to_y", 9);
        vertx.eventBus().send("routing.fromto", jsonObject, new Handler<Message<JsonArray>>() {
            @Override
            public void handle(Message<JsonArray> reply) {
                assertTrue(reply.body().toString().equals("[[1,1],[1,2],[1,3],[2,4],[3,5],[4,6],[5,7],[6,8],[7,9],[8,9]]"));
                testComplete();
            }
        });
    }

    @Test
    public void testSomethingElse() {
        // Needed but I don't know why
        testComplete();
    }


    @Override
    public void start() {
        // Make sure we call initialize() - this sets up the assert stuff so assert functionality works correctly
        initialize();
        // Deploy the module - the System property `vertx.modulename` will contain the name of the module so you
        // don't have to hardecode it in your tests
        container.deployModule(System.getProperty("vertx.modulename"), new AsyncResultHandler<String>() {
            @Override
            public void handle(AsyncResult<String> asyncResult) {
                // Deployment is asynchronous and this this handler will be called when it's complete (or failed)
                if (asyncResult.failed()) {
                    container.logger().error(asyncResult.cause());
                }
                assertTrue(asyncResult.succeeded());
                assertNotNull("deploymentID should not be null", asyncResult.result());
                // If deployed correctly then start the tests!
                startTests();
            }
        });
    }

}
