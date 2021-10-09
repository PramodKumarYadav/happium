package org.saucedemo.factories.devices;

import lombok.extern.slf4j.Slf4j;

/*
We had a unique problem statement here. Since for parallel execution in appium, you can only run a test on a single device,
we had this challenge on how to get a unique device name from all the tests that are running in parallel. Each test thread
was trying to get the device(0). So what we want is, that at the device selection (below method), all the threads
should run in sequence. Synchronized keyword makes that possible. Below is the article that shows how.
http://tutorials.jenkov.com/java-concurrency/synchronized.html
 */

@Slf4j
public class AvailableDevices {
    private static Integer deviceNumber = 0;
    private static Integer emulatorNumber = 5554;
    private static Integer systemPort = 8200;

    public static synchronized Device getAndroidEmulator(){
        Device device = new Device();

        log.info("fetching device number: {}", deviceNumber);
        String deviceName = AndroidEmulators.values()[deviceNumber].toString();
        log.info("device fetched: {}", deviceName);

        // Set all the unique properties for this emulator device (necessary for execution in parallel)
        device.setDeviceName(deviceName);
        device.setUdid("emulator-" + emulatorNumber);
        device.setSystemPort(systemPort);

        deviceNumber ++;
        systemPort ++;
        emulatorNumber = emulatorNumber + 2;

        log.info("Device: {}", device);
        return device;
    }

    public static synchronized String getIosSimulator(){
        log.info("fetching device number: {}", deviceNumber);
        String deviceName = IosSimulators.values()[deviceNumber].toString();
        log.info("device fetched: {}", deviceName);

        deviceNumber ++;
        return deviceName;
    }
}

