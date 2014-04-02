package de.codepitbull.worlddomination.mcp.controller
/**
 *
 * @author Jochen Mader
 */
class RobotInfo {
    float x
    float y
    String direction
    String name
    Closure handler

    RobotInfo(String parms) {
        def startParams = parms.split(",")
        name = startParams[0]
        x = startParams[1].toFloat()
        y = startParams[2].toFloat()
        direction = startParams[3]
    }


    @Override
    public java.lang.String toString() {
        return "RobotInfo{" +
                "x=" + x +
                ", y=" + y +
                ", direction='" + direction + '\'' +
                ", name='" + name + '\'' +
                ", handler=" + handler +
                '}';
    }
}
