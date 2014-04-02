package de.codepitbull.worlddomination.mcp

import org.vertx.groovy.platform.Verticle

class PongVerticle extends Verticle {
    @Override
    def start() {
        vertx.eventBus.registerHandler("ping", { message ->
            container.logger.error("RECEIVED PING: ${message.body()}")
        })
    }
}
