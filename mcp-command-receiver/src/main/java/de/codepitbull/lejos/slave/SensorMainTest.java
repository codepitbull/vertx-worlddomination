package de.codepitbull.lejos.slave;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.MindsensorsAbsoluteIMU;
import lejos.hardware.sensor.SensorMode;

/**
 * @author Jochen Mader.
 */
public class SensorMainTest {
    public static void main(String[] args) {
        float[] res = new float[3];
        MindsensorsAbsoluteIMU imu = new MindsensorsAbsoluteIMU(SensorPort.S1);
        SensorMode rotationSensorMode = imu.getRateMode();
        int count = 0;
        float val1 = 0;
        float val2 = 0;
        float val3 = 0;
        while (true) {
            count++;
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            rotationSensorMode.fetchSample(res, 0);
            val1 += res[0];
            val2 += res[1];
            val3 += res[2];
            if(count == 33) {
                System.out.println(val1/33+" "+val2/33+" "+val3/33);
                count = 0;
                val1 = 0;
                val2 = 0;
                val3 = 0;
            }
        }
    }

}
