package de.codepitbull.lejos.slave.controller;

import de.codepitbull.lejos.slave.sensors.CollisionListener;
import de.codepitbull.lejos.slave.sensors.DistanceMeasureRunnable;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.MindsensorsAbsoluteIMU;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;

/**
 * @author Jochen Mader
 */
public class RobotController implements CollisionListener{

    private final RegulatedMotor gunMotor;
    private final DifferentialPilot differentialPilot;
    private final SampleProvider ultraSonicSampleProvider;
    private final float amountOfDegreesPerTurn;
    private final float[] distanceSample = new float[]{0};
    private volatile boolean stoppedToPreventCollision = false;
    private final DistanceMeasureRunnable distanceMeasureRunnable;

    RobotController(Builder builder) {
        gunMotor = builder.gun;
        differentialPilot = new DifferentialPilot(builder.wheelDiameter, builder.wheelDistance, builder.left, builder.right);
        differentialPilot.setAcceleration(builder.accelerationMotor);
        differentialPilot.setTravelSpeed(builder.speedMotor);
        differentialPilot.setRotateSpeed(builder.rotateSpeed);
        amountOfDegreesPerTurn = 90+builder.rotationErrorInDegrees;
        ultraSonicSampleProvider = builder.ultraSonic.getDistanceMode();
        distanceMeasureRunnable = builder.distanceMeasureRunnable;
        distanceMeasureRunnable.setCollisionListener(this);
    }

    public void fireGun(Integer times) {
        gunMotor.rotate(360 * 3 * times);
    }

    public boolean travel(Float distanceInCm) {
        stoppedToPreventCollision = false;
        Thread collisionWarner = new Thread(distanceMeasureRunnable);
        collisionWarner.start();
        differentialPilot.travel(distanceInCm);
        distanceMeasureRunnable.stop();
        return !stoppedToPreventCollision;
    }

    public void rotateRight(int nrRotations) {
        differentialPilot.rotate(-amountOfDegreesPerTurn*nrRotations);
    }

    public void rotateLeft(int nrRotations) {
        differentialPilot.rotate(amountOfDegreesPerTurn*nrRotations);
    }

    public float measureDistance() {
        ultraSonicSampleProvider.fetchSample(distanceSample, 0);
        return distanceSample[0];
    }

    @Override
    public void collisionImminent() {
        stoppedToPreventCollision = true;
        differentialPilot.quickStop();
    }

    public static final class Builder {
        float speedMotor = 180;
        int accelerationMotor = 6000;
        float wheelDiameter = 32.5f;
        float wheelDistance = 170.0f;
        float rotateSpeed = 90;
        float rotationErrorInDegrees = 10;

        RegulatedMotor left;
        RegulatedMotor right;
        RegulatedMotor gun;
        EV3UltrasonicSensor ultraSonic;
        MindsensorsAbsoluteIMU imu;
        DistanceMeasureRunnable distanceMeasureRunnable;

        public Builder() {
        }

        public Builder setRotateSpeed(float rotateSpeed) {
            this.rotateSpeed = rotateSpeed;
            return this;
        }

        public Builder setSpeedMotor(float speedMotor) {
            this.speedMotor = speedMotor;
            return this;
        }

        public Builder setAccelerationMotor(int accelerationMotor) {
            this.accelerationMotor = accelerationMotor;
            return this;
        }

        public Builder setWheelDiameter(float wheelDiameter) {
            this.wheelDiameter = wheelDiameter;
            return this;
        }

        public Builder setWheelDistance(float wheelDistance) {
            this.wheelDistance = wheelDistance;
            return this;
        }

        public Builder setRotationErrorInDegrees(float rotationErrorInDegrees) {
            this.rotationErrorInDegrees = rotationErrorInDegrees;
            return this;
        }

        public Builder setDistanceMeasureRunnable(DistanceMeasureRunnable distanceMeasureRunnable) {
            this.distanceMeasureRunnable = distanceMeasureRunnable;
            return this;
        }

        public Builder setLeft(RegulatedMotor left) {
            this.left = left;
            return this;
        }

        public Builder setRight(RegulatedMotor right) {
            this.right = right;
            return this;
        }

        public Builder setGun(RegulatedMotor gun) {
            this.gun = gun;
            return this;
        }

        public Builder setUltraSonic(EV3UltrasonicSensor ultraSonic) {
            this.ultraSonic = ultraSonic;
            return this;
        }

        public Builder setImu(MindsensorsAbsoluteIMU imu) {
            this.imu = imu;
            return this;
        }

        public RobotController build() {
            if(left == null)
                throw new RuntimeException("left not set");
            if(right == null)
                throw new RuntimeException("right not set");
            if(gun == null)
                throw new RuntimeException("gun not set");
            if(ultraSonic == null)
                throw new RuntimeException("ultrarSonic not set");
            if(imu == null)
                throw new RuntimeException("imu not set");
            if(distanceMeasureRunnable == null)
                throw new RuntimeException("distanceMeasureRunnable not set");
            return new RobotController(this);
        }
    }
}
