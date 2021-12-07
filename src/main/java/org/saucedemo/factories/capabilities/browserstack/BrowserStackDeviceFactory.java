package org.saucedemo.factories.capabilities.browserstack;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.saucedemo.enums.Platform;
import org.saucedemo.factories.EnvFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Random;

@NoArgsConstructor
@Slf4j
public class BrowserStackDeviceFactory {
    private static final String DEVICE = EnvFactory.getConfig().getString("DEVICE");
    private static final String OS_VERSION = EnvFactory.getConfig().getString("OS_VERSION");

    private static final Platform PLATFORM = Platform.valueOf(EnvFactory.getConfig().getString("PLATFORM_NAME"));

    private static final String DEVICE_FILE_PATH_FORMAT = "%s/%s.csv";
    private static final String BROWSERSTACK_ANDROID_DEVICES_PATH = EnvFactory.getConfig().getString("BROWSERSTACK_ANDROID_DEVICES_PATH");
    private static final String BROWSERSTACK_IOS_DEVICES_PATH = EnvFactory.getConfig().getString("BROWSERSTACK_IOS_DEVICES_PATH");

    /**
     * All devices (fixed or random, are to be picked from this list):
     * https://www.browserstack.com/list-of-browsers-and-platforms/app_automate
     */
    public BrowserStackDevice getDevice() {
        if (DEVICE.equalsIgnoreCase("random")) {
            return getARandomBrowserStackDevice(getDeviceFilePath());
        } else if (EnumUtils.isValidEnumIgnoreCase(AvailableAndroidModels.class, DEVICE) || EnumUtils.isValidEnumIgnoreCase(AvailableIOSModels.class, DEVICE)) {
            return getARandomBrowserStackDevice(getDeviceFilePath(DEVICE));
        } else {
            return getAFixedBrowserStackDevice();
        }
    }

    private static final BrowserStackDevice getAFixedBrowserStackDevice() {
        return new BrowserStackDevice(DEVICE, OS_VERSION);
    }

    private String getDeviceFilePath() {
        switch (PLATFORM) {
            case android:
                return String.format(DEVICE_FILE_PATH_FORMAT, BROWSERSTACK_ANDROID_DEVICES_PATH, AvailableAndroidModels.getRandomModel().getValue());
            case ios:
                return String.format(DEVICE_FILE_PATH_FORMAT, BROWSERSTACK_IOS_DEVICES_PATH, AvailableIOSModels.getRandomModel().getValue());
            default:
                throw new IllegalStateException("Platform choice is incorrect. You can either choose 'android' or 'ios'." +
                        "Check the value of 'PLATFORM_NAME' property set in application.conf; Or in CI, if run from continuous integration.");
        }
    }

    private String getDeviceFilePath(String modelType) {
        log.info("Model type: {}", modelType);
        switch (PLATFORM) {
            case android:
                if (EnumUtils.isValidEnumIgnoreCase(AvailableAndroidModels.class, modelType)) {
                    return String.format(DEVICE_FILE_PATH_FORMAT, BROWSERSTACK_ANDROID_DEVICES_PATH, modelType);
                } else {
                    throw new IllegalStateException(String.format("android does not have %s devices. Fix your platform or device choice", modelType));
                }
            case ios:
                if (EnumUtils.isValidEnumIgnoreCase(AvailableIOSModels.class, modelType)) {
                    return String.format(DEVICE_FILE_PATH_FORMAT, BROWSERSTACK_IOS_DEVICES_PATH, modelType);
                } else {
                    throw new IllegalStateException(String.format("ios does not have %s devices. Fix your platform or device choice", modelType));
                }
            default:
                throw new IllegalStateException("Platform choice is incorrect. You can either choose 'android' or 'ios'." +
                        "Check the value of 'PLATFORM_NAME' property set in application.conf; Or in CI, if run from continuous integration.");
        }
    }

    private BrowserStackDevice getARandomBrowserStackDevice(String filePath) {
        List<BrowserStackDevice> devices = getDevicesForAModel(filePath);
        return getRandomDeviceFromAParticularModel(devices);
    }

    private static List<BrowserStackDevice> getDevicesForAModel(String csvFilePath) {
        try {
            return new CsvToBeanBuilder(new FileReader(csvFilePath))
                    .withType(BrowserStackDevice.class)
                    .build()
                    .parse();
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("csvFilePath not found.", e);
        }
    }


    private BrowserStackDevice getRandomDeviceFromAParticularModel(List<BrowserStackDevice> devices) {
        Random random = new Random();
        Integer randomDevice = random.nextInt(devices.size());
        return devices.get(randomDevice);
    }
}
