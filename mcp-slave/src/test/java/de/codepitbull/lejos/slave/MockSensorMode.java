package de.codepitbull.lejos.slave;

import lejos.hardware.sensor.SensorMode;

/**
* @author Jochen Mader
*/
public class MockSensorMode implements SensorMode {

    private float zAmount = 0;

    public MockSensorMode(float zAmount) {
        this.zAmount = zAmount;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public int sampleSize() {
        return 3;
    }

    @Override
    public void fetchSample(float[] floats, int i) {
        floats[0] = 0;
        floats[1] = 0;
        floats[2] = zAmount;
    }
}
