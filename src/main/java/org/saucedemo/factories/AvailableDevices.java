package org.saucedemo.factories;

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

    public static synchronized String getDevice(){
        log.info("fetching device number: {}", deviceNumber);
        String deviceName = DeviceList.values()[deviceNumber].toString();
        log.info("device fetched: {}", deviceName);

        deviceNumber ++;
        return deviceName;
    }
}

