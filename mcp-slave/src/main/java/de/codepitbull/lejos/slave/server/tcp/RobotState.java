package de.codepitbull.lejos.slave.server.tcp;

import de.codepitbull.lejos.slave.server.commands.Direction;
import de.codepitbull.lejos.slave.controller.RobotController;

public class RobotState {
    public final RobotController robotController;
    public Float x, y;
    public Direction direction;

    public RobotState(RobotController robotController) {
        this.robotController = robotController;
    }
}