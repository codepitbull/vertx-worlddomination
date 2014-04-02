package de.codepitbull.lejos.slave;

import lejos.hardware.motor.Motor;
import lejos.robotics.navigation.DifferentialPilot;

/**
 * 325 1700 90 91 91
 * @author Jochen Mader
 */
public class DifferentialMainTest {
    public static void main(String[] args) throws Exception{
        int diameter = Integer.parseInt(args[0]);
        int distance = Integer.parseInt(args[1]);
        float rotspeed = Float.parseFloat(args[2]);
        float left = Float.parseFloat(args[3]);
        float right = Float.parseFloat(args[4]);
        DifferentialPilot differentialPilot = new DifferentialPilot(diameter,distance, Motor.A, Motor.D);
        differentialPilot.setRotateSpeed(rotspeed);
        System.out.println("l - turn left : r - turn right");
        while(true) {
            int in = System.in.read();
            if(in=='l') {
                differentialPilot.rotate(left);
            }
            else if(in=='r') {
                differentialPilot.rotate(-right);
            }
        }
    }
}
