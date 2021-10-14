package org.saucedemo.factories.devices;

import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.saucedemo.factories.EnvConfigFactory;
import org.saucedemo.runmodes.ExecutionModes;

import java.util.HashMap;

import static org.saucedemo.runmodes.ExecutionMode.getExecutionMode;

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

    // Create a map where you can add any device that is already used by a test class
    private static HashMap<String, Device> testClassDevicesMap = new HashMap<>();
    private static String currentTestClass = "";

    /*
    Pick a device (fixed or random) based on the choice provided in application.conf
    If user wants to pick any random device. Then get a random device.
    Else, keep the deviceName provided by user in application.conf file. In case if user wants to test with a specific device.
    Note: that if you do provide a fixed deviceName, then you cannot run tests in parallel.
    So change parallel property to false in junit-platform.properties file.
    junit.jupiter.execution.parallel.enabled=false (for parallel mode keep this true and deviceName = randomDevice
    */
    public static synchronized Device getAndroidEmulator(String testClassName) {
        Device device = new Device();
        ExecutionModes mode = getExecutionMode();
        switch (mode) {
            case CLASS_SERIES_TEST_SERIES:
                device = getASpecificAndroidEmulator();
                break;
            case CLASS_SERIES_TEST_PARALLEL:
                device = getAUniqueAndroidEmulatorPerTestWithinAClass(testClassName);
                break;
            case CLASS_PARALLEL_TEST_SERIES:
                device = getAUniqueAndroidEmulatorPerTestClass(testClassName);
                break;
            case CLASS_PARALLEL_TEST_PARALLEL:
                device = getAUniqueAndroidEmulator();
                break;
            default:
                break;
        }

        return device;
    }

    // A convenience method to get the device name from application.conf file.
    // Say when running tests in series in a particular class.
    public static synchronized Device getASpecificAndroidEmulator() {
        Config config = EnvConfigFactory.getConfig();
        String deviceName = config.getString("deviceName");
        return getASpecificAndroidEmulator(deviceName);
    }

    // If in future, we need to pass on the deviceName, then we will get rid of convenience method and can use this.
    public static synchronized Device getASpecificAndroidEmulator(String deviceName) {
        Device device = new Device();

        // Set all the unique properties for this emulator device (necessary for execution in parallel)
        device.setDeviceName(deviceName);
        device.setUdid("emulator-" + emulatorNumber);
        device.setSystemPort(systemPort);

        log.info("Device details: {}", device);
        return device;
    }

    public static synchronized Device getAUniqueAndroidEmulatorPerTestWithinAClass(String testClassName) {
        // If this is a new test class than initialize variables so that you can pick devices from beginning.
        if (! testClassName.equalsIgnoreCase(currentTestClass)) {
            log.info("New testClass tests started for: {}", testClassName);
            currentTestClass = testClassName;

            // Initialize all variables
            deviceNumber = 0;
            emulatorNumber = 5554;
            systemPort = 8200;
        }

        log.info("Fetching a unique device for this test!");
        return getAUniqueAndroidEmulator();
    }

    public static synchronized Device getAUniqueAndroidEmulatorPerTestClass(String testClassName) {
        if (testClassDevicesMap.containsKey(testClassName)) {
            log.info("device already available.");
            log.info("Device details: {}", testClassDevicesMap.get(testClassName));
            return testClassDevicesMap.get(testClassName);
        } else {
            log.info("device not already available. Fetch a unique one for test class {}", testClassName);
            Device device = getAUniqueAndroidEmulator();
            testClassDevicesMap.put(testClassName, device);
            return device;
        }
    }

    // Say when running tests in parallel within a single class.
    public static synchronized Device getAUniqueAndroidEmulator() {
        Device device = new Device();

        log.info("fetching device number: {}", deviceNumber);
        String deviceName = AndroidEmulators.values()[deviceNumber].toString();

        // Set all the unique properties for this emulator device (necessary for execution in parallel)
        device.setDeviceName(deviceName);
        device.setUdid("emulator-" + emulatorNumber);
        device.setSystemPort(systemPort);

        // Increment so that next device fetched has unique properties. 
        deviceNumber++;
        systemPort++;
        emulatorNumber = emulatorNumber + 2;

        log.info("Device details: {}", device);
        return device;
    }

    public static synchronized String getIosSimulator() {
        log.info("fetching device number: {}", deviceNumber);
        String deviceName = IosSimulators.values()[deviceNumber].toString();
        log.info("device fetched: {}", deviceName);

        deviceNumber++;
        return deviceName;
    }
}

