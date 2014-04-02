package de.codepitbull.worlddomination.mcp

import org.vertx.groovy.platform.Verticle

class MainVerticle extends Verticle {
    @Override
    Object start() {
        container.deployModule("io.vertx~mod-web-server~2.0.0-final", container.config)
        container.deployVerticle("groovy:" + BrowserCommunicationVerticle.class.getCanonicalName())
        container.deployVerticle("groovy:" + PongVerticle.class.getCanonicalName())
    }
}
