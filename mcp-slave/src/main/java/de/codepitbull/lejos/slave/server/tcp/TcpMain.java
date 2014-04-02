package de.codepitbull.lejos.slave.server.tcp;

import de.codepitbull.lejos.slave.server.commands.Direction;
import de.codepitbull.lejos.slave.controller.RobotController;
import de.codepitbull.lejos.slave.sensors.DistanceMeasureRunnable;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.MindsensorsAbsoluteIMU;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * @author Jochen Mader
 */
public class TcpMain {
    public static void main(String[] args) throws Exception{
        if(args.length == 0) {
            System.out.println("Usage: TcpMain <properties-file>");
            System.exit(0);
        }

        Properties properties = new Properties();
        properties.load(new FileInputStream(new File(args[0])));

        EV3UltrasonicSensor ev3UltrasonicSensor = new EV3UltrasonicSensor(SensorPort.S4);

        RobotController robotController = new RobotController.Builder()
                .setLeft(Motor.A)
                .setRight(Motor.D)
                .setGun(Motor.B)
                .setUltraSonic(ev3UltrasonicSensor)
                .setImu(new MindsensorsAbsoluteIMU(SensorPort.S1))
                .setDistanceMeasureRunnable(new DistanceMeasureRunnable(ev3UltrasonicSensor, 0.20f))
                .build();

        RobotServerRunnable robotServerRunnable = new RobotServerRunnable.Builder()
                .setName(properties.getProperty("name"))
                .setHost(properties.getProperty("host"))
                .setPort(Integer.parseInt(properties.getProperty("port")))
                .setReconnectInterval(Integer.parseInt(properties.getProperty("reconnectInterval")))
                .setX(Float.parseFloat(properties.getProperty("x")))
                .setY(Float.parseFloat(properties.getProperty("y")))
                .setDirection(Direction.valueOf(properties.getProperty("direction")))
                .setRobotController(robotController)
                .build();

        Thread serverThread = new Thread(robotServerRunnable);
        serverThread.start();
        serverThread.join();
    }
}
