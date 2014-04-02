package de.codepitbull.worlddomination.routing.nav

import groovy.transform.CompileStatic

/**
 *
 * @author Jochen Mader
 */
@CompileStatic
class WeightedPosition implements Comparable<WeightedPosition>{

    final Integer x,y;
    final Integer value;
    WeightedPosition predecessor
    Double costToGetHere = 0
    Double distance = 0

    public WeightedPosition(Integer x, Integer y, Integer value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    @Override
    int compareTo(WeightedPosition o) {
        Double diff = (costToGetHere+distance)-(o.costToGetHere+o.distance)
        if(diff<0)
            return -1
        if(diff>0)
            return 1
        return 0
    }

    @Override
    public java.lang.String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                ", value=" + value +
                '}';
    }
}
