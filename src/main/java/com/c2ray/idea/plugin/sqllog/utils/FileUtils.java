package com.c2ray.idea.plugin.sqllog.utils;

import java.io.*;
import java.nio.file.Files;

/**
 * @author c2ray
 * @since 2023/5/24
 */
public class FileUtils {

    public static File createTmpFile(String tmpFileName, String suffix, InputStream inputStream) throws IOException {
        tmpFileName = String.join("/", PathUtils.getTmpPath(), tmpFileName);
        File tempFile = File.createTempFile(tmpFileName, suffix);
        try (inputStream;
             OutputStream tos = Files.newOutputStream(tempFile.toPath());
             BufferedOutputStream bos = new BufferedOutputStream(tos)) {
            bos.write(inputStream.readAllBytes());
        }
        tempFile.deleteOnExit();
        return tempFile;
    }

}
