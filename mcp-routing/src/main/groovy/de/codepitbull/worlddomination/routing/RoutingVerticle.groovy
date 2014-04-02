package de.codepitbull.worlddomination.routing

import de.codepitbull.worlddomination.routing.nav.AStarBasedPathfinder
import de.codepitbull.worlddomination.routing.nav.EnvironmentMapWithWeight
import de.codepitbull.worlddomination.routing.nav.WeightedPosition
import org.vertx.groovy.core.eventbus.Message
import org.vertx.groovy.platform.Verticle
import org.vertx.java.core.json.JsonArray
import org.vertx.java.core.shareddata.ConcurrentSharedMap

/**
 *
 * @author Jochen Mader
 */
class RoutingVerticle extends Verticle {

    @Override
    Object start() {
        vertx.eventBus.registerHandler("routing.fromto") { Message message ->
            if(message.body() in Map) {
                Map obj = (Map)message.body()

                Integer fromX = obj["from_x"]
                Integer fromY = obj["from_y"]
                Integer toX = obj["to_x"]
                Integer toY = obj["to_y"]

                ConcurrentSharedMap<Integer, String> environmentMap = vertx.sharedData.getMap("environmentMap")
                EnvironmentMapWithWeight weightedMap = new EnvironmentMapWithWeight(environmentMap)
                WeightedPosition start = weightedMap.get(fromX, fromY)
                WeightedPosition stop = weightedMap.get(toX, toY)
                List<WeightedPosition> route = new AStarBasedPathfinder().route(start, stop, weightedMap)
                List<List<Integer>> routeFinal = new ArrayList<>()
                route.each { WeightedPosition pos ->
                    routeFinal.add([pos.x, pos.y])
                }
                message.reply(new JsonArray(routeFinal))
            }
            else {
                container.logger.error("Expected a Map but got "+message.body())
            }


        }
    }
}
