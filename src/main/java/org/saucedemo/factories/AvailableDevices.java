package org.saucedemo.factories;

import lombok.extern.slf4j.Slf4j;

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
