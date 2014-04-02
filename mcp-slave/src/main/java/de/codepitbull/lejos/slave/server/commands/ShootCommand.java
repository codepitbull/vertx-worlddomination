package de.codepitbull.lejos.slave.server.commands;

import de.codepitbull.lejos.slave.server.tcp.RobotState;

import static de.codepitbull.lejos.slave.command.CommandBuilder.answer;

/**
 * @author Jochen Mader
 */
public class ShootCommand implements Command<Integer>{
    private final RobotState robotState;

    public ShootCommand(RobotState robotState) {
        this.robotState = robotState;
    }

    @Override
    public String execute(String amount, String commandId) {
        robotState.robotController.fireGun(Integer.valueOf(amount));
        return answer(commandId, true, amount);
    }
}
