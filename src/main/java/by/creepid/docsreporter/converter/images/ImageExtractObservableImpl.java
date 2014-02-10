/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.converter.images;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author rusakovich
 */
public class ImageExtractObservableImpl implements ImageExtractObservable {

    private final Set<ImageExtractObserver> observers;

    public ImageExtractObservableImpl() {
        observers = new HashSet<>();
    }

    @Override
    public void fireExtractImageEvent(byte[] content, String path) {
        Iterator<ImageExtractObserver> iterator = observers.iterator();
        while (iterator.hasNext()) {
            iterator.next().getImage(content, path);
        }
    }

    @Override
    public void addImageExtractObserver(ImageExtractObserver observer) {
        if (observer != null) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(ImageExtractObserver observer) {
        if (observer != null && observers.contains(observer)) {
            observers.remove(observer);
        }
    }

}
