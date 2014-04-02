package de.codepitbull.worlddomination.mcp.mustache;

import org.junit.Test;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.testtools.TestVerticle;

import static org.vertx.testtools.VertxAssert.assertEquals;
import static org.vertx.testtools.VertxAssert.testComplete;

public class MustacheVerticleTest extends TestVerticle{

    @Test
    public void testRender() {
        container.deployVerticle("groovy:"+MustacheRendererVerticle.class.getName(), new Handler<AsyncResult<String>>() {
            @Override
            public void handle(AsyncResult<String> event) {
                JsonObject renderTemplate = new JsonObject()
                        .putString("template", "robots")
                        .putArray("content", new JsonArray().add("robby"));
                vertx.eventBus().send("template.render", renderTemplate, new Handler<Message<String>>() {
                    @Override
                    public void handle(Message<String> response) {
                        assertEquals("<p class=\"lead\">robby</p>\n", response.body());
                        testComplete();
                    }
                });
            }
        });
    }
}