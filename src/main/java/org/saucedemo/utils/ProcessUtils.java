package org.saucedemo.utils;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ProcessUtils {
    public static Process runProcess(String command) {
        try {
            return Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(command + "failed");
        }
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
