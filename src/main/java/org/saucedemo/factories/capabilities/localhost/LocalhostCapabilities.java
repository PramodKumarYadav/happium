package org.saucedemo.factories.capabilities.localhost;

import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.saucedemo.choices.Platform;
import org.saucedemo.executionmodes.ExecutionFactory;
import org.saucedemo.executionmodes.ExecutionMode;
import org.saucedemo.factories.EnvFactory;
import org.saucedemo.factories.capabilities.localhost.android.AvailableEmulators;
import org.saucedemo.factories.capabilities.localhost.android.EmulatorDevice;

import java.util.Iterator;
import java.util.List;

import static org.saucedemo.factories.capabilities.localhost.android.EmulatorDevicePicker.getAndroidEmulator;
import static org.saucedemo.utils.FileUtils.getCanonicalPath;
import static org.saucedemo.utils.FileUtils.getFileAsString;

@Slf4j
public class LocalhostCapabilities {
    private static Config config = EnvFactory.getInstance().getConfig();
    private static final List<String> availableEmulators = AvailableEmulators.getInstance().getAll();

    public static DesiredCapabilities get(Platform platform) {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        String DEVICE_TYPE = config.getString("LOCALHOST.DEVICE_TYPE");
        String DEVICE_NAME = config.getString("LOCALHOST.DEVICE_NAME");

        // On localhost you are either on android or on IOS (not both).
        log.info("Running tests on DEVICE_TYPE: {}", DEVICE_TYPE);
        log.info("Running tests on DEVICE_NAME: {}", DEVICE_NAME);

        switch (platform) {
            case ANDROID:
                // Get local app location stored in the project here (via absolute path)
                capabilities.setCapability("app", getCanonicalPath(config.getString("PATH_ANDROID_APP")));

                // Set common android capabilities here from the config file
                capabilities = setAndroidCommonCapabilities(capabilities);

                // Set capabilities specific for device type (fixed or virtual)
                switch (DEVICE_TYPE) {
                    case "real":
                        setAndroidRealDeviceCapabilities(DEVICE_NAME, capabilities);
                        break;
                    case "virtual":
                        isEmulatorExecutionRequirementMet();
                        setAndroidEmulatorCapabilities(capabilities);
                        break;
                    default:
                        break;
                }
                break;
            case IOS:
                // Get local app location stored in the project here (via absolute path)
                capabilities.setCapability("app", getCanonicalPath(config.getString("PATH_IOS_APP")));

                // Set common android capabilities here from the config file
                capabilities = setIosCommonCapabilities(capabilities);

                // Set capabilities specific for device type (fixed or virtual)
                switch (DEVICE_TYPE) {
                    case "real":
                        setIosRealDeviceCapabilities(DEVICE_NAME, capabilities);
                        break;
                    case "virtual":
                        setIosSimulatorCapabilities(capabilities);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }

        log.debug("Capabilities: {}", capabilities);
        return capabilities;
    }

    private static DesiredCapabilities setAndroidCommonCapabilities(DesiredCapabilities capabilities) {
        setCapabilitiesFromFile(config.getString("PATH_ANDROID_COMMON_CAPABILITIES"), capabilities);
        return capabilities;
    }

    private static DesiredCapabilities setIosCommonCapabilities(DesiredCapabilities capabilities) {
        setCapabilitiesFromFile(config.getString("PATH_IOS_COMMON_CAPABILITIES"), capabilities);
        return capabilities;
    }

    // This is when you want to run tests on a Single real android device connected to your computer.
    // So no synchronized required (since tests will run in sequence). Remember to put the parallel run property to false in junit-platform.properties
    private static DesiredCapabilities setAndroidRealDeviceCapabilities(String deviceName, DesiredCapabilities capabilities) {
        String pathDeviceNameConfig = String.format("%s/%s.json", config.getString("PATH_ANDROID_CAPABILITIES"), deviceName);

        setCapabilitiesFromFile(pathDeviceNameConfig, capabilities);
        return capabilities;
    }

    private static DesiredCapabilities setIosRealDeviceCapabilities(String deviceName, DesiredCapabilities capabilities) {
        String pathDeviceNameConfig = String.format("%s/%s.json", config.getString("PATH_IOS_CAPABILITIES"), deviceName);

        setCapabilitiesFromFile(pathDeviceNameConfig, capabilities);
        return capabilities;
    }

    /*
    Apart from fetching devices in a synchronized way (in a parallel run mode), we also had this challenge
     that the next steps to use this fetched device name to get capabilities and setting up avd, were getting affected
     in the race condition of parallel threads competing.
     This is the reason, we have to encapsulate all three steps of:
     1) finding an available emulator.
     2) setting capabilities for this emulator.
     3) setting avd for this device name as a synchronized block.
     In absence of this, the same device was getting picked by multiple threads running in parallel.
    http://tutorials.jenkov.com/java-concurrency/synchronized.html
    */
    private static synchronized DesiredCapabilities setAndroidEmulatorCapabilities(DesiredCapabilities capabilities) {
        setCapabilitiesFromFile(config.getString("PATH_ANDROID_EMULATOR_DEFAULT_CAPABILITIES"), capabilities);

        // getAndroidEmulator method contains logic to decide if user wants a 'specific' device or a 'random' device.
        // or "unique devices per test" within one class OR "unique device per each test class".
        EmulatorDevice emulatorDevice = getAndroidEmulator();
        capabilities.setCapability("avd", emulatorDevice.getDeviceName());
        capabilities.setCapability("udid", emulatorDevice.getUdid());
        capabilities.setCapability("deviceName", emulatorDevice.getUdid());
        capabilities.setCapability("appium:systemPort  ", emulatorDevice.getSystemPort());

        return capabilities;
    }

    // todo: when you pick up IOS work.
    private static synchronized DesiredCapabilities setIosSimulatorCapabilities(DesiredCapabilities capabilities) {
        return null;
    }

    private static DesiredCapabilities setCapabilitiesFromFile(String filePath, DesiredCapabilities capabilities) {
        log.info("parsing desired capabilities from: {}", filePath);

        String jsonString = getFileAsString(filePath);

        JSONObject obj = new JSONObject(jsonString);
        Iterator<?> keys = obj.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String value = obj.get(key).toString();

            log.debug("key: {}", key);
            log.debug("value: {}", value);

            capabilities.setCapability(key, value);
        }

        return capabilities;
    }

    public static void isEmulatorExecutionRequirementMet() {
        ExecutionMode executionMode = ExecutionFactory.getInstance().getExecutionMode();
        switch (executionMode) {
            case CLASS_SERIES_TEST_SERIES:
                if(availableEmulators.size() < 1){
                    throw new IllegalStateException("No emulators found to run tests in series");
                }
                break;
            case CLASS_SERIES_TEST_PARALLEL:
                // fall through - same validation applies for all parallel execution modes.
            case CLASS_PARALLEL_TEST_SERIES:
                // fall through - same validation applies for all parallel execution modes.
            case CLASS_PARALLEL_TEST_PARALLEL:
                String configStrategy = ExecutionFactory.getInstance().getConfigStrategy();
                if(configStrategy.equalsIgnoreCase("fixed")){
                    if(availableEmulators.size() < ExecutionFactory.getInstance().getFixedThreadCount()){
                        throw new IllegalStateException("Nr of emulators are less than total thread count. Either reduce threads or add more emulators");
                    }
                }else{ // So when the strategy is not fixed but say is 'dynamic'
                    throw new IllegalStateException("Running with config strategy that is not 'fixed' is not recommended for mobile tests. " +
                            "You would need as many emulators as there are nr of tests in the project. Your CPU would not be able to handle that kind of load." +
                            "Note: If you still wish to go ahead with strategy 'dynamic', then comment this exception.");
                }
            default:
                break;
        }
    }
}
