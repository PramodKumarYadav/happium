package org.saucedemo.deviceTypes.browserStack;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.saucedemo.deviceTypes.browserStack.android.AndroidModelType;
import org.saucedemo.factories.EnvConfigFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
public class BrowserStackDevicePicker {
    /** All devices (fixed or random, are to be picked from this list):
     * https://www.browserstack.com/list-of-browsers-and-platforms/app_automate */
    public static synchronized BrowserStackDevice getDevice() {
        String PLATFORM_NAME = EnvConfigFactory.getConfig().getString("PLATFORM_NAME");
        String MODEL_TYPE = EnvConfigFactory.getConfig().getString("MODEL_TYPE");
        String DEVICE = EnvConfigFactory.getConfig().getString("DEVICE");
        String OS_VERSION = EnvConfigFactory.getConfig().getString("OS_VERSION");

        // If a fixed device is specified in browserstack appconfig to be picked; use that.
        if (! DEVICE.equals("random")) {
            BrowserStackDevice device = new BrowserStackDevice(DEVICE, OS_VERSION);
            return device;
        }

        // If a random model is asked, get a random model else use the modelType that was provided to get a random device
        if (MODEL_TYPE.equals("random")) {
            MODEL_TYPE = AndroidModelType.getRandomModel().getValue();
            log.info("Model type: {}", MODEL_TYPE);
        }

        String filePath = String.format("./desired-capabilities/browserstack/%s/%s.csv", PLATFORM_NAME, MODEL_TYPE);
        List<BrowserStackDevice> devices = BrowserStackDevicePicker.getDevicesForAModel(filePath);
        return getRandomDevice(devices);
    }

    private static BrowserStackDevice getRandomDevice(List<BrowserStackDevice> devices) {
        Random random = new Random();
        Integer randomDevice = random.nextInt(devices.size());
        return devices.get(randomDevice);
    }

    public static final List<BrowserStackDevice> getDevicesForAModel(String csvFilePath) {
        List<BrowserStackDevice> devices = new ArrayList<>();
        try {
            devices = new CsvToBeanBuilder(new FileReader(csvFilePath))
                    .withType(BrowserStackDevice.class)
                    .build()
                    .parse();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return devices;
    }
}
