package org.saucedemo.hosts.browserStack;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.saucedemo.factories.TestEnvironment;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Random;

@Slf4j
public class BrowserStackDevicePicker {

    /**
     * All devices (fixed or random, are to be picked from this list):
     * https://www.browserstack.com/list-of-browsers-and-platforms/app_automate
     */
    public static synchronized BrowserStackDevice getDevice() {
        String DEVICE = TestEnvironment.getConfig().getString("DEVICE").toUpperCase();
        if (DEVICE.equals("RANDOM")) {
            return getARandomBrowserStackDevice(getDeviceFilePath());
        } else if (EnumUtils.isValidEnum(AvailableAndroidModels.class, DEVICE) || EnumUtils.isValidEnum(AvailableIOSModels.class, DEVICE)) {
            return getARandomBrowserStackDevice(getDeviceFilePath(DEVICE));
        } else {
            return getAFixedBrowserStackDevice();
        }
    }

    private static BrowserStackDevice getAFixedBrowserStackDevice() {
        String DEVICE = TestEnvironment.getConfig().getString("DEVICE");
        String OS_VERSION = TestEnvironment.getConfig().getString("OS_VERSION");
        BrowserStackDevice device = new BrowserStackDevice(DEVICE, OS_VERSION);
        return device;
    }

    private static String getDeviceFilePath(String modelType) {
        String PLATFORM_NAME = TestEnvironment.getConfig().getString("PLATFORM_NAME");
        log.info("Model type: {}", modelType);
        String basePath;
        switch (PLATFORM_NAME) {
            case "android":
                basePath = TestEnvironment.getConfig().getString("BROWSERSTACK_ANDROID_DEVICES_PATH");
                if (EnumUtils.isValidEnum(AvailableAndroidModels.class, modelType)) {
                    return String.format("%s/%s.csv", basePath, modelType);
                } else {
                    throw new IllegalStateException(String.format("android does not have %s devices. Fix your platform or device choice", modelType));
                }
            case "ios":
                basePath = TestEnvironment.getConfig().getString("BROWSERSTACK_IOS_DEVICES_PATH");
                if (EnumUtils.isValidEnum(AvailableIOSModels.class, modelType)) {
                    return String.format("%s/%s.csv", basePath, modelType);
                } else {
                    throw new IllegalStateException(String.format("ios does not have %s devices. Fix your platform or device choice", modelType));
                }
            default:
                throw new IllegalStateException("Platform choice is incorrect. You can either choose 'android' or 'ios'." +
                        "Check the value of 'PLATFORM_NAME' property set in application.conf; Or in CI, if run from continuous integration.");
        }
    }

    private static String getDeviceFilePath() {
        String PLATFORM_NAME = TestEnvironment.getConfig().getString("PLATFORM_NAME");
        String basePath;
        String modelType;
        switch (PLATFORM_NAME) {
            case "android":
                basePath = TestEnvironment.getConfig().getString("BROWSERSTACK_ANDROID_DEVICES_PATH");
                modelType = AvailableAndroidModels.getRandomModel().getValue();
                return String.format("%s/%s.csv", basePath, modelType);
            case "ios":
                basePath = TestEnvironment.getConfig().getString("BROWSERSTACK_IOS_DEVICES_PATH");
                modelType = AvailableIOSModels.getRandomModel().getValue();
                return String.format("%s/%s.csv", basePath, modelType);
            default:
                throw new IllegalStateException("Platform choice is incorrect. You can either choose 'android' or 'ios'." +
                        "Check the value of 'PLATFORM_NAME' property set in application.conf; Or in CI, if run from continuous integration.");
        }
    }

    private static BrowserStackDevice getARandomBrowserStackDevice(String filePath) {
        List<BrowserStackDevice> devices = BrowserStackDevicePicker.getDevicesForAModel(filePath);
        return getRandomDeviceFromAParticularModel(devices);
    }

    public static final List<BrowserStackDevice> getDevicesForAModel(String csvFilePath) {
        List<BrowserStackDevice> devices;
        try {
            devices = new CsvToBeanBuilder(new FileReader(csvFilePath))
                    .withType(BrowserStackDevice.class)
                    .build()
                    .parse();
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("csvFilePath not found.", e);
        }

        return devices;
    }

    private static BrowserStackDevice getRandomDeviceFromAParticularModel(List<BrowserStackDevice> devices) {
        Random random = new Random();
        Integer randomDevice = random.nextInt(devices.size());
        return devices.get(randomDevice);
    }
}
