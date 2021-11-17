package org.saucedemo.host_devices.localhost.android;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EmulatorDevice {
    private String udid;
    private String deviceName;
    private Long systemPort;
}
