package org.saucedemo.hosts.browserStack;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.saucedemo.hosts.browserStack.android.AndroidModelType;
import org.saucedemo.factories.EnvConfigFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Random;

@Slf4j
public class BrowserStackDevicePicker {
    /** All devices (fixed or random, are to be picked from this list):
     * https://www.browserstack.com/list-of-browsers-and-platforms/app_automate */
    public static synchronized BrowserStackDevice getDevice() {
        String DEVICE = EnvConfigFactory.getConfig().getString("DEVICE");
        if (DEVICE.equals("random")) {
            return getARandomBrowserStackDevice();
        }else {
            return getAFixedBrowserStackDevice();
        }
    }

    private static BrowserStackDevice getAFixedBrowserStackDevice() {
        String DEVICE = EnvConfigFactory.getConfig().getString("DEVICE");
        String OS_VERSION = EnvConfigFactory.getConfig().getString("OS_VERSION");
        BrowserStackDevice device = new BrowserStackDevice(DEVICE, OS_VERSION);
        return device;
    }

    private static BrowserStackDevice getARandomBrowserStackDevice() {
        // If a random model is asked, get a random model else use the modelType that was provided to get a random device
        String MODEL_TYPE = EnvConfigFactory.getConfig().getString("MODEL_TYPE");
        if (MODEL_TYPE.equals("random")) {
            MODEL_TYPE = AndroidModelType.getRandomModel().getValue();
        }
        log.info("Model type: {}", MODEL_TYPE);

        String BROWSERSTACK_DEVICES_PATH = EnvConfigFactory.getConfig().getString("BROWSERSTACK_DEVICES_PATH");
        String filePath = String.format("%s%s.csv", BROWSERSTACK_DEVICES_PATH, MODEL_TYPE);
        List<BrowserStackDevice> devices = BrowserStackDevicePicker.getDevicesForAModel(filePath);

        return getRandomDeviceFromAParticularModel(devices);
    }

    private static BrowserStackDevice getRandomDeviceFromAParticularModel(List<BrowserStackDevice> devices) {
        Random random = new Random();
        Integer randomDevice = random.nextInt(devices.size());
        return devices.get(randomDevice);
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
}
