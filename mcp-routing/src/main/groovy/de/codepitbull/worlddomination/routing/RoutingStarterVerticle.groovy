/*
 * Copyright 2013 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 *
 */

package de.codepitbull.worlddomination.routing

import groovy.transform.CompileStatic
import org.vertx.groovy.platform.Verticle
import org.vertx.java.core.shareddata.ConcurrentSharedMap

@CompileStatic
class RoutingStarterVerticle extends Verticle {

    @Override
    Object start() {
        Integer worldSize = 16
        if(container.config.containsKey("worldsize"))
            worldSize = (Integer)container.config.get("worldsize")

        ConcurrentSharedMap<Integer, String> map = vertx.sharedData.getMap("environmentMap")
        map.put(0,"0,0,0,0,0,0,0,0,0,0")
        map.put(1,"0,0,0,0,0,0,0,0,0,0")
        map.put(2,"0,0,0,0,0,0,0,0,0,0")
        map.put(3,"0,0,1,1,1,1,0,0,0,0")
        map.put(4,"0,0,0,0,0,1,0,0,0,0")
        map.put(5,"0,0,0,0,0,1,0,0,0,0")
        map.put(6,"0,0,0,0,0,1,0,0,0,0")
        map.put(7,"0,0,0,0,0,0,0,0,0,0")
        map.put(8,"0,0,0,0,0,0,0,0,0,0")
        map.put(9,"0,0,0,0,0,0,0,0,0,0")

        container.deployVerticle("groovy:"+RoutingVerticle.class.getCanonicalName())
    }

}
