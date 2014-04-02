package de.codepitbull.worlddomination.mcp.controller

import org.vertx.groovy.core.buffer.Buffer
import org.vertx.groovy.core.net.NetSocket
import org.vertx.groovy.platform.Verticle
import org.vertx.java.core.json.JsonArray

class ControllerVerticle extends Verticle {

    static final String ROBOTS_LIST_NAME = "robots"
    long commandCounter = 0
    def robots = [:]

    @Override
    def start() {
        vertx.createNetServer().connectHandler { NetSocket socket ->

            def name = ""

            socket.dataHandler { Buffer buffer ->
                String received = buffer.getString(0, buffer.length)
                received = received.substring(0, received.length() - 1)
                if (received.startsWith("robot:")) {
                    RobotInfo robotInfo = new RobotInfo(received.split(":")[1])
                    name = robotInfo.name
                    robots[name] = socket
                    vertx.sharedData.getSet(ROBOTS_LIST_NAME).add(name)
                    vertx.eventBus.publish("robots.announce", name)
                    container.logger.info("New robot connected: ${name} ${robotInfo.x} ${robotInfo.y} ${robotInfo.direction}")
                }
            }

            socket.closeHandler {
                robots.remove(name)
                vertx.sharedData.getSet(ROBOTS_LIST_NAME).remove(name)
                vertx.eventBus.publish("robots.disconnect", name)
                container.logger.info("Robot disconnected: ${name}")
            }

        }.listen(2000)

        vertx.eventBus.registerHandler("robots.list") { message ->
            message.reply(new JsonArray(vertx.sharedData.getSet(ROBOTS_LIST_NAME).toArray()))
        };

        vertx.eventBus.registerHandler("robots.command", { message ->
            container.logger.info("Received command: ${message.body()}")
            def nameAndCommand = message.body().split(":")
            def socket = robots[nameAndCommand[0]]
            if(socket !=null) {
                commandCounter++
                nameAndCommand[1].split(" ").each {
                    switch ( it ) {
                        case "forward" : socket.write(CommandBuilder.travel(200, commandCounter.toString())); break;
                        case "shoot" : socket.write(CommandBuilder.shoot(1, commandCounter.toString())); break;
                        case "left" : socket.write(CommandBuilder.left(1, commandCounter.toString())); break;
                        case "right" : socket.write(CommandBuilder.right(1, commandCounter.toString())); break;
                    }
                }
            }
        });
    }


}
