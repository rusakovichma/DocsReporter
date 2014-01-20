/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 *
 * @author rusakovich
 */
public final class FileUtils {

    private FileUtils() {
    }

    public static File getFile(String filePath) {
        File outFile = new File(filePath);

        if (!outFile.exists()) {
            outFile.getParentFile().mkdirs();
        }

        return outFile;
    }

    public static OutputStream getOutputStream(String filePath)
            throws FileNotFoundException {
        File outFile = getFile(filePath);

        return new FileOutputStream(outFile);
    }

}
