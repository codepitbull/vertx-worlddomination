package de.codepitbull.lejos.slave.server.commands;

import de.codepitbull.lejos.slave.server.tcp.RobotState;

import static de.codepitbull.lejos.slave.command.CommandBuilder.answer;

/**
 * @author Jochen Mader
 */
public class TravelCommand implements Command<Integer>{
    private final RobotState robotState;

    public TravelCommand(RobotState robotState) {
        this.robotState = robotState;
    }

    @Override
    public String execute(String amount, String commandId) {
        Float distance = Float.valueOf(amount);
        if(robotState.direction.equals(Direction.N))
            robotState.y = robotState.y+distance;
        else if(robotState.direction.equals(Direction.S))
            robotState.y = robotState.y-distance;
        if(robotState.direction.equals(Direction.E))
            robotState.x = robotState.x+distance;
        else if(robotState.direction.equals(Direction.W))
            robotState.x = robotState.x-distance;
        return answer(commandId, robotState.robotController.travel(distance), amount);
    }
}
