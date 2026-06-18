package com.threebrowsers.selenium.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

public class FileUtil {

    /**
     * Elimina una carpeta y todo su contenido de forma segura para entornos CI/CD y locales.
     */
    public static void deleteFolder(File folder) {
        if (folder == null || !folder.exists()) {
            return;
        }

        Path pathToBeDeleted = folder.toPath();

        try (Stream<Path> walk = Files.walk(pathToBeDeleted)) {
            walk.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(f -> {
                        // Intentamos limpiar la recolecta de basura antes por si el OS retiene punteros a las imágenes
                        if (f.isFile()) {
                            System.gc();
                        }
                        if (!f.delete() && f.exists()) {
                            Logs.warning("[CI/CD FILE WARN] Could not delete file (possibly in use): " + f.getAbsolutePath());
                        }
                    });

            Logs.info("Folder and previous execution history securely cleaned: " + folder.getAbsolutePath());
        } catch (IOException e) {
            Logs.error("[CI/CD FILE ERROR] Error attempting to empty the reports directory: " + e.getMessage());
        }
    }
}