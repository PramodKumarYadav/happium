package org.saucedemo.hosts.browserStack;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BrowserStackDevice {
    @CsvBindByName(column = "Device Name")
    private String deviceName;

    @CsvBindByName(column = "OS Version")
    private String osVersion;
}
