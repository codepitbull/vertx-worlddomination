package de.codepitbull.lejos.slave.server.local;

import de.codepitbull.lejos.slave.controller.RobotController;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.MindsensorsAbsoluteIMU;

/**
 * @author Jochen Mader
 */
public class LocalMain {
    public static void main(String[] args) throws Exception{
        EV3UltrasonicSensor us = new EV3UltrasonicSensor(SensorPort.S4);
        RobotController robotController = new RobotController.Builder()
                .setLeft(Motor.A)
                .setRight(Motor.D)
                .setGun(Motor.B)
                .setUltraSonic(new EV3UltrasonicSensor(SensorPort.S4))
                .setImu(new MindsensorsAbsoluteIMU(SensorPort.S1))
                .build();
        System.out.println("l - turn left\nr - turn right\nm - measure distance\nt - travel 20 cm");
        float[] vals = new float[]{0};
        while(true) {
            int in = System.in.read();
            if(in=='l') {
                robotController.rotateLeft(1);
                System.out.println("turned left");
            }
            else if(in=='r') {
                robotController.rotateRight(1);
                System.out.println("turned right");
            }
            else if(in=='m') {
                System.out.println("traveled 20: "+robotController.travel(20f));
            }
            else if(in=='m') {
                System.out.println("dist "+robotController.measureDistance());
            }
        }
    }
}
