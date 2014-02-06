/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.converter;

import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author rusakovich
 */
public interface ImageExtractor {

    public void addImage(String path, byte[] content);

    public Iterator<Map.Entry<String, byte[]>> getImageIterator();

}
