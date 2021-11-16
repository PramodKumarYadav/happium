package org.saucedemo.hosts.localhost.android;

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
    private Long systemPort;

}
