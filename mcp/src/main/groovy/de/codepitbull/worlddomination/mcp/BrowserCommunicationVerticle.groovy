package de.codepitbull.worlddomination.mcp

import org.vertx.groovy.platform.Verticle
import org.vertx.java.core.json.JsonObject

/**
 *
 * @author Jochen Mader
 */
class BrowserCommunicationVerticle extends Verticle{

    @Override
    Object start() {
        vertx.eventBus.registerHandler("robots.status") { message ->
            vertx.eventBus.send("robots.list", "", { response ->
                JsonObject renderTemplate = new JsonObject()
                        .putString("template", "robots")
                        .putArray("content", response.body())
                vertx.eventBus.send("template.render", renderTemplate, { renderedResponse ->
                    message.reply(renderedResponse.body())
                })
            })
        };

    }
}
