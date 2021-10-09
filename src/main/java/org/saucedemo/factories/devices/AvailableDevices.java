package org.saucedemo.factories.devices;

import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.saucedemo.factories.EnvConfigFactory;

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

    /*
    Pick a device (fixed or random) based on the choice provided in application.conf
    If user wants to pick any random device. Then get a random device.
    Else, keep the deviceName provided by user in application.conf file. In case if user wants to test with a specific device.
    Note: that if you do provide a fixed deviceName, then you cannot run tests in parallel.
    So change parallel property to false in junit-platform.properties file.
    junit.jupiter.execution.parallel.enabled=false (for parallel mode keep this true and deviceName = randomDevice
    */
    public static synchronized Device getAndroidEmulator(){
        // todo: Add logic here to check if unique device is needed at a class level, or test level or both.
        Config config = EnvConfigFactory.getConfig();
        String deviceName = config.getString("deviceName");

        Device device = new Device();
        if (deviceName.equalsIgnoreCase("randomVirtualDevice")) {
            device = getRandomAndroidEmulator();
        }else {
            device = getSpecificAndroidEmulator(deviceName);
        }

        return device;
    }

    // Say when running tests in series in a particular class.
    public static synchronized Device getSpecificAndroidEmulator(String deviceName){
        Device device = new Device();

        // Set all the unique properties for this emulator device (necessary for execution in parallel)
        device.setDeviceName(deviceName);
        device.setUdid("emulator-" + emulatorNumber);
        device.setSystemPort(systemPort);

        log.info("Device details: {}", device);
        return device;
    }

    // Say when running tests in parallel within a single class.
    // todo : OR say, when running tests in parallel on multiple classes (within each class, whereas tests will then run in sequence)
    public static synchronized Device getRandomAndroidEmulator(){
        Device device = new Device();

        log.info("fetching device number: {}", deviceNumber);
        String deviceName = AndroidEmulators.values()[deviceNumber].toString();

        // Set all the unique properties for this emulator device (necessary for execution in parallel)
        device.setDeviceName(deviceName);
        device.setUdid("emulator-" + emulatorNumber);
        device.setSystemPort(systemPort);

        // Increment so that next device fetched has unique properties. 
        deviceNumber ++;
        systemPort ++;
        emulatorNumber = emulatorNumber + 2;

        log.info("Device details: {}", device);
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

