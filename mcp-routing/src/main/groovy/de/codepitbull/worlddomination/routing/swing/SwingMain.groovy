package de.codepitbull.worlddomination.routing.swing

import de.codepitbull.worlddomination.routing.nav.EnvironmentMapWithWeight

/**
 *
 * @author Jochen Mader
 */
class SwingMain {
    public static void main(String[] args) {
        Map<Integer, String> map = new HashMap<>();
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
        EnvironmentMapWithWeight environmentMap = new EnvironmentMapWithWeight(map);

        new MapWindow(environmentMap);
    }

}
