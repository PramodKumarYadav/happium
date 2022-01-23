package org.saucedemo.factories.capabilities.localhost.android;

import org.saucedemo.utils.ProcessUtils;

import java.util.List;

public class AvailableEmulators {
    /**
     * With this approach, we are relying on JVM to create the unique instance of EnvFactory when the class is loaded.
     * The JVM guarantees that the instance will be created before any thread accesses the static uniqueInstance variable.
     * This code is thus guaranteed to be thread safe.
     */
    private static AvailableEmulators uniqueInstance = new AvailableEmulators();
    private static Process process;
    private static final List<String> availableEmulators = ProcessUtils.getProcessResult(process);

    private AvailableEmulators() {
        process = ProcessUtils.runProcess("emulator -list-avds");
    }

    public static AvailableEmulators getInstance() {
        return uniqueInstance;
    }

    public List<String> getAll() {
        return availableEmulators;
    }
}
