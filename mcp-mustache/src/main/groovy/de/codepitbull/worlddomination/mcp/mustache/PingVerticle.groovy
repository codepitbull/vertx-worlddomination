package de.codepitbull.worlddomination.mcp.mustache

import org.vertx.groovy.platform.Verticle

class PingVerticle extends Verticle {
    @Override
    def start() {
        vertx.setPeriodic(500, {
            vertx.eventBus.send("ping","ping")
        })
    }
}
