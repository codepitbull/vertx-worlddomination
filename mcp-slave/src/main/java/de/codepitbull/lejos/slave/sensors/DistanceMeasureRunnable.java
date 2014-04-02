package de.codepitbull.lejos.slave.sensors;

import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

/**
 * @author Jochen Mader
 */
public class DistanceMeasureRunnable implements Runnable {

    private static final int NUMBER_OF_SAMPLES = 4;
    private final SampleProvider distanceMode;
    private final float minimumDistance;
    private volatile boolean stop = false;
    private float[] sensorResults = new float[]{0};
    private final float[] sampleContainer = new float[NUMBER_OF_SAMPLES];
    private volatile CollisionListener collisionListener;
    int arrayIndex = 0;

    public DistanceMeasureRunnable(EV3UltrasonicSensor ultrasonicSensor, float minimumDistance) {
        this.minimumDistance = minimumDistance;
        this.distanceMode = ultrasonicSensor.getDistanceMode();
    }

    @Override
    public void run() {
        stop = false;
        cleanSampleContainer();
        while(!stop) {
            try {
                Thread.sleep(30);
            }
            catch (InterruptedException e) {
            }
            distanceMode.fetchSample(sensorResults, 0);
            sampleContainer[arrayIndex] = sensorResults[0];
            arrayIndex++;
            if(arrayIndex==NUMBER_OF_SAMPLES) {
                arrayIndex=0;
            }

            if(isCollisionImminent()) {
                collisionListener.collisionImminent();
                break;
            }
        }
    }

    private boolean isCollisionImminent() {
        int positiveIfCollisionIsimminent = 0;
        for(int arrayIndex=0;arrayIndex<NUMBER_OF_SAMPLES;arrayIndex++) {
            if(sampleContainer[arrayIndex] < minimumDistance)
                positiveIfCollisionIsimminent++;
            else
                positiveIfCollisionIsimminent--;
        }
        return positiveIfCollisionIsimminent>0;
    }

    private void cleanSampleContainer() {
        for(int arrayIndex=0;arrayIndex<NUMBER_OF_SAMPLES;arrayIndex++) {
            sampleContainer[arrayIndex] = minimumDistance;
        }
    }

    public void stop() {
        this.stop = true;
    }

    public void setCollisionListener(CollisionListener collisionListener) {
        this.collisionListener = collisionListener;
    }
}
