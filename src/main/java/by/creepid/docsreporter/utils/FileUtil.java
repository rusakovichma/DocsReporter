/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.utils;

import java.io.File;

/**
 *
 * @author rusakovich
 */
public final class FileUtil {

    private FileUtil() {
    }

    public static boolean isFileExist(String path) {
        File temp = new File(path);

        return temp.exists();
    }

}
