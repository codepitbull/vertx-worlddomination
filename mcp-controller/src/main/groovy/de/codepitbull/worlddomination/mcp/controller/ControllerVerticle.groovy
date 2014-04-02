package de.codepitbull.worlddomination.mcp.controller

import org.vertx.groovy.core.buffer.Buffer
import org.vertx.groovy.core.net.NetSocket
import org.vertx.groovy.platform.Verticle
import org.vertx.java.core.json.JsonArray

class ControllerVerticle extends Verticle {

    static final String ROBOTS_LIST_NAME = "robots"
    long commandCounter = 0
    def robots = []

    @Override
    def start() {

        vertx.eventBus.registerHandler("robots.alive", { message ->
            def name = message.body()
            if(!robots.contains(name)) {
                robots.add(message.body())
                vertx.sharedData.getSet(ROBOTS_LIST_NAME).add(name)
                container.logger.info("New robot connected: ${name}")
            }

        });

        vertx.eventBus.registerHandler("robots.list") { message ->
            message.reply(new JsonArray(vertx.sharedData.getSet(ROBOTS_LIST_NAME).toArray()))
        };

        vertx.eventBus.registerHandler("robots.command", { message ->
            container.logger.info("Received command: ${message.body()}")
            def nameAndCommand = message.body().split(":")
            commandCounter++
            nameAndCommand[1].split(" ").each {
                def command = ""
                switch ( it ) {
                    case "forward" :  command = CommandBuilder.travel(200, commandCounter.toString()); break;
                    case "shoot" : command = CommandBuilder.shoot(1, commandCounter.toString()); break;
                    case "left" : command =  CommandBuilder.left(1, commandCounter.toString()); break;
                    case "right" : command =  CommandBuilder.right(1, commandCounter.toString()); break;
                }
                vertx.eventBus.send("${nameAndCommand[0]}.commands", command)
            }
        });
    }


}
