package org.saucedemo.hosts.browserstack;

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
    private BrowserStackDevicePicker() {
        throw new IllegalStateException("Utility class");
    }

    public static final String FILE_FORMAT = "%s/%s.csv";

    /**
     * All devices (fixed or random, are to be picked from this list):
     * https://www.browserstack.com/list-of-browsers-and-platforms/app_automate
     */
    public static synchronized BrowserStackDevice getDevice() {
        String device = TestEnvironment.getConfig().getString("DEVICE").toUpperCase();
        if (device.equals("RANDOM")) {
            return getARandomBrowserStackDevice(getDeviceFilePath());
        } else if (EnumUtils.isValidEnum(AvailableAndroidModels.class, device) || EnumUtils.isValidEnum(AvailableIOSModels.class, device)) {
            return getARandomBrowserStackDevice(getDeviceFilePath(device));
        } else {
            return getAFixedBrowserStackDevice();
        }
    }

    private static BrowserStackDevice getAFixedBrowserStackDevice() {
        String device = TestEnvironment.getConfig().getString("DEVICE");
        String osVersion = TestEnvironment.getConfig().getString("OS_VERSION");
        return new BrowserStackDevice(device, osVersion);
    }

    private static String getDeviceFilePath(String modelType) {
        String platformName = TestEnvironment.getConfig().getString("PLATFORM_NAME");
        log.info("Model type: {}", modelType);
        String basePath;
        switch (platformName) {
            case "android":
                basePath = TestEnvironment.getConfig().getString("BROWSERSTACK_ANDROID_DEVICES_PATH");
                if (EnumUtils.isValidEnum(AvailableAndroidModels.class, modelType)) {
                    return String.format(FILE_FORMAT, basePath, modelType);
                } else {
                    throw new IllegalStateException(String.format("android does not have %s devices. Fix your platform or device choice", modelType));
                }
            case "ios":
                basePath = TestEnvironment.getConfig().getString("BROWSERSTACK_IOS_DEVICES_PATH");
                if (EnumUtils.isValidEnum(AvailableIOSModels.class, modelType)) {
                    return String.format(FILE_FORMAT, basePath, modelType);
                } else {
                    throw new IllegalStateException(String.format("ios does not have %s devices. Fix your platform or device choice", modelType));
                }
            default:
                throw new IllegalStateException("Platform choice is incorrect. You can either choose 'android' or 'ios'." +
                        "Check the value of 'PLATFORM_NAME' property set in application.conf; Or in CI, if run from continuous integration.");
        }
    }

    private static String getDeviceFilePath() {
        String platformName = TestEnvironment.getConfig().getString("PLATFORM_NAME");
        String basePath;
        String modelType;
        switch (platformName) {
            case "android":
                basePath = TestEnvironment.getConfig().getString("BROWSERSTACK_ANDROID_DEVICES_PATH");
                modelType = AvailableAndroidModels.getRandomModel().getValue();
                return String.format(FILE_FORMAT, basePath, modelType);
            case "ios":
                basePath = TestEnvironment.getConfig().getString("BROWSERSTACK_IOS_DEVICES_PATH");
                modelType = AvailableIOSModels.getRandomModel().getValue();
                return String.format(FILE_FORMAT, basePath, modelType);
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
        try {
            return new CsvToBeanBuilder(new FileReader(csvFilePath))
                    .withType(BrowserStackDevice.class)
                    .build()
                    .parse();
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("csvFilePath not found.", e);
        }
    }


    private static BrowserStackDevice getRandomDeviceFromAParticularModel(List<BrowserStackDevice> devices) {
        Random random = new Random();
        Integer randomDevice = random.nextInt(devices.size());
        return devices.get(randomDevice);
    }
}
