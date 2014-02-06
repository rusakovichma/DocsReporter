/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.converter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author rusakovich
 */
public class ImageExtractorImpl implements ImageExtractor {

    protected Map<String, byte[]> images;

    public ImageExtractorImpl() {
        images = new HashMap<>();
    }

    @Override
    public void addImage(String path, byte[] content) {
        images.put(path, content);
    }

    @Override
    public Iterator<Map.Entry<String, byte[]>> getImageIterator() {
        return images.entrySet().iterator();
    }

}
