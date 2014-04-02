package de.codepitbull.worlddomination.routing.nav

import groovy.transform.CompileStatic

/**
 *
 * @author Jochen Mader
 */
@CompileStatic
class EnvironmentMapWithWeight {
    List<WeightedPosition> map;
    Integer sideLength

    EnvironmentMapWithWeight(Map<Integer, String> mapWithStrings) {

        int count=0
        map = new ArrayList<>()
        sideLength = mapWithStrings.size()
        while(mapWithStrings.containsKey(count)) {
            String mapRow = mapWithStrings.get(count)
            mapRow.split(",").eachWithIndex { String value, int index ->
                map.add(new WeightedPosition(index, count, value.asType(Integer)))
            }
            count++
        }

    }

    WeightedPosition get(Integer x, Integer y) {
        return map.get(x + y * sideLength)
    }

}
