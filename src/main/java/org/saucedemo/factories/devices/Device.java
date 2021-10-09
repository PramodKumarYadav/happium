package org.saucedemo.factories.devices;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Device {
    private String udid;
    private String deviceName;
    private Integer systemPort;

}
