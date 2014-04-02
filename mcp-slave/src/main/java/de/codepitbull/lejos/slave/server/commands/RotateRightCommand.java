package de.codepitbull.lejos.slave.server.commands;

import de.codepitbull.lejos.slave.server.tcp.RobotState;

import static de.codepitbull.lejos.slave.command.CommandBuilder.answer;

/**
 * @author Jochen Mader
 */
public class RotateRightCommand implements Command<Integer>{
    private final RobotState robotState;

    public RotateRightCommand(RobotState robotState) {
        this.robotState = robotState;
    }

    @Override
    public String execute(String amount, String commandId) {
        robotState.robotController.rotateRight(Integer.valueOf(amount));
        robotState.direction=robotState.direction.calculateNewDirection(1);
        return answer(commandId, true, amount);
    }
}
