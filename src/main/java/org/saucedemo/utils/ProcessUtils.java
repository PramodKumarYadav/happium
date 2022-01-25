package org.saucedemo.utils;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ProcessUtils {
    @SneakyThrows
    public static Process runProcess(String command) {
        return Runtime.getRuntime().exec(command);
    }

    @SneakyThrows
    public static List<String> getProcessResult(Process process) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        List<String> commandResultAsList = new ArrayList<>();
        String line = "";
        while ((line = reader.readLine()) != null) {
            commandResultAsList.add(line);
        }

        return commandResultAsList;
    }
}
