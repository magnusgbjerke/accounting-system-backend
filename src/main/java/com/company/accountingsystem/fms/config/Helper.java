package com.company.accountingsystem.fms.config;

import java.io.File;
import java.util.List;

public class Helper {

    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        // Check if the file exists and is actually a file
        if (file.exists() && file.isFile()) {
            boolean deleted = file.delete();
            if (!deleted) {
                throw new IllegalStateException("Failed to delete file: " + file.getAbsolutePath());
            }
        } else {
            throw new IllegalStateException("File not found or not a regular file: " + file.getAbsolutePath());
        }
    }

    public static void deleteDirectory(File file) {
        // Don't delete sample files
        List<String> skipFiles = SkipFiles.skipFiles;
        for (File subfile : file.listFiles()) {
            // Skip files in the skipFiles list
            if (skipFiles.contains(subfile.getAbsolutePath())) {
                continue;
            }

            // Skip the "config" folder and all its contents
            if (subfile.isDirectory() && subfile.getName().equalsIgnoreCase("config")) {
                continue;
            }

            // Recursively delete subfolders
            if (subfile.isDirectory()) {
                deleteDirectory(subfile);
            }

            // Delete files and empty subfolders
            subfile.delete();
        }
    }
}
