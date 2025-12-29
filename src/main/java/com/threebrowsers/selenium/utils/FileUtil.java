package com.threebrowsers.selenium.utils;

import java.io.File;

public class FileUtil {

    public static void deleteFolder(File folder) {
        if (folder.exists()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        deleteFolder(f);
                    } else {
                        f.delete();
                    }
                }
            }
            folder.delete();
            Logs.info("Carpeta eliminada: " + folder.getAbsolutePath());
        }
    }
}
