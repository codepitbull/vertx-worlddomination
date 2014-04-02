package de.codepitbull.lejos.slave;

import de.codepitbull.lejos.slave.controller.RobotController;
import de.codepitbull.lejos.slave.sensors.DistanceMeasureRunnable;
import de.codepitbull.lejos.slave.server.commands.Direction;
import de.codepitbull.lejos.slave.server.tcp.RobotServerRunnable;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.MindsensorsAbsoluteIMU;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import static org.mockito.Matchers.anyFloat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Jochen Mader
 */
public class TcpMockMain {
    public static void main(String[] args) throws Exception{
        if(args.length == 0) {
            System.out.println("Usage: TcpMockMain <properties-file>");
            System.exit(0);
        }

        Properties properties = new Properties();
        properties.load(new FileInputStream(new File(args[0])));

        RobotController robotController = mock(RobotController.class);
        when(robotController.measureDistance()).thenReturn(25f);
        when(robotController.travel(anyFloat())).thenReturn(Boolean.TRUE);

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
