package de.codepitbull.lejos.slave.verticle;

import de.codepitbull.lejos.slave.command.CommandEnum;
import de.codepitbull.lejos.slave.controller.RobotController;
import de.codepitbull.lejos.slave.sensors.DistanceMeasureRunnable;
import de.codepitbull.lejos.slave.server.commands.*;
import de.codepitbull.lejos.slave.server.tcp.RobotState;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.MindsensorsAbsoluteIMU;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.platform.Verticle;

import java.util.HashMap;
import java.util.Map;

import static de.codepitbull.lejos.slave.command.CommandBuilder.separator;

/**
 * @author Jochen Mader
 */
public class MainVerticle extends Verticle{
    private Map<CommandEnum, Command<?>> commandEnumCommandMap = new HashMap<>();
    private RobotState robotState;
    private String name="robby";

    @Override
    public void start() {
        EV3UltrasonicSensor ev3UltrasonicSensor = new EV3UltrasonicSensor(SensorPort.S4);

        RobotController robotController = new RobotController.Builder()
                .setLeft(Motor.A)
                .setRight(Motor.D)
                .setGun(Motor.B)
                .setUltraSonic(ev3UltrasonicSensor)
                .setImu(new MindsensorsAbsoluteIMU(SensorPort.S1))
                .setDistanceMeasureRunnable(new DistanceMeasureRunnable(ev3UltrasonicSensor, 0.20f))
                .build();

        this.robotState = new RobotState(robotController);
        this.robotState.x = 0f;
        this.robotState.y = 0f;
        this.robotState.direction = Direction.N;
        commandEnumCommandMap.put(CommandEnum.MEASURE, new MeasureCommand(robotState));
        commandEnumCommandMap.put(CommandEnum.TRAVEL, new TravelCommand(robotState));
        commandEnumCommandMap.put(CommandEnum.RIGHT, new RotateRightCommand(robotState));
        commandEnumCommandMap.put(CommandEnum.LEFT, new RotateLeftCommand(robotState));
        commandEnumCommandMap.put(CommandEnum.SHOOT, new ShootCommand(robotState));

        vertx.eventBus().registerHandler(name+".commands", new Handler<Message<String>>() {
            @Override
            public void handle(Message<String> event) {
                String[] nameAndValueAndCommandId = event.body().split(separator);
                event.reply(execute(nameAndValueAndCommandId[0], nameAndValueAndCommandId[1], nameAndValueAndCommandId[2]));
            }
        });

        vertx.setPeriodic(1000,new Handler<Long>() {
            @Override
            public void handle(Long event) {
                vertx.eventBus().publish("robots.alive",name);
            }
        });

    }

    private String execute(String command, String amount, String commandId) {
        CommandEnum actualCommand = CommandEnum.commandFromString(command);
        if(actualCommand != null) {
            return commandEnumCommandMap.get(actualCommand).execute(amount, commandId);
        }
        else {
            container.logger().warn("Unknown command: " + command + " " + amount);
        }
        return "1:0:\n";
    }
}
