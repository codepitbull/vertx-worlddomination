package de.codepitbull.lejos.slave.server.commands;

import java.util.*;

/**
 * @author Jochen Mader
 */
public enum Direction{
    N(1),E(2),S(3),W(4);

    private static final List<Direction> directions = Collections.unmodifiableList(Arrays.asList(Direction.values()));
    public static Direction getByVal(int val) { return directions.get(val); }

    private final int val;

    private Direction(int val) {
        this.val = val;
    }

    public int getVal() { return val; }

    public Direction calculateNewDirection(int rotation) {
        return directions.get((val + rotation) % 4);
    }
}
