package de.codepitbull.lejos.slave.controller;

import de.codepitbull.lejos.slave.sensors.DistanceMeasureRunnable;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.MindsensorsAbsoluteIMU;
import lejos.robotics.RegulatedMotor;
import org.junit.Test;

import static org.mockito.Mockito.mock;

/**
 * @author Jochen Mader
 */
public class RobotControllerTest {

    @Test
    public void testCreateRobotController() {
        DistanceMeasureRunnable distanceMeasureRunnable = mock(DistanceMeasureRunnable.class);
        RegulatedMotor left = mock(RegulatedMotor.class);
        RegulatedMotor right = mock(RegulatedMotor.class);
        RegulatedMotor gun = mock(RegulatedMotor.class);
        EV3UltrasonicSensor ultrasonicSensor = mock(EV3UltrasonicSensor.class);
        MindsensorsAbsoluteIMU imu = mock(MindsensorsAbsoluteIMU.class);
        new RobotController.Builder()
                .setLeft(left)
                .setRight(right)
                .setGun(gun)
                .setUltraSonic(ultrasonicSensor)
                .setImu(imu)
                .setDistanceMeasureRunnable(distanceMeasureRunnable)
                .build();
    }

}
