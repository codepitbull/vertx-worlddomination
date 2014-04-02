package de.codepitbull.worlddomination.mcp.mustache

import org.vertx.groovy.platform.Verticle

class MainVerticle extends Verticle {
    @Override
    def start() {
        container.deployVerticle("groovy:" + MustacheRendererVerticle.class.getCanonicalName())
        container.deployVerticle("groovy:" + PingVerticle.class.getCanonicalName())
    }
}
