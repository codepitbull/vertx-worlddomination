package de.codepitbull.lejos.slave.server.commands;

import de.codepitbull.lejos.slave.server.tcp.RobotState;

import static de.codepitbull.lejos.slave.command.CommandBuilder.answer;

/**
 * @author Jochen Mader
 */
public class MeasureCommand implements Command<Integer>{
    private final RobotState robotState;

    public MeasureCommand(RobotState robotState) {
        this.robotState = robotState;
    }

    @Override
    public String execute(String amount, String commandId) {
        return answer(commandId, true, Float.toString(robotState.robotController.measureDistance()));
    }
}
