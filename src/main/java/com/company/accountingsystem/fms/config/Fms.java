package com.company.accountingsystem.fms.config;

import java.io.File;
import java.nio.file.Paths;

public class Fms {
    public static String fmsPath() {
        String currentPath = Paths.get("").toAbsolutePath().toString();
        return currentPath + File.separator + "src" + File.separator + "main" + File.separator + "java" +
                File.separator + "com" + File.separator + "company" + File.separator + "accountingsystem" +
                File.separator + "fms" + File.separator;
    }

    public static void deleteFmsDirectory() {
        File file = new File(Fms.fmsPath());
        Helper.deleteDirectory(file);
    }
}
