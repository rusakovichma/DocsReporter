/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.converter.images;

/**
 *
 * @author rusakovich
 */
public interface ImageExtractObservable {

    public void fireExtractImageEvent(byte[] content, String path);

    public void addImageExtractObserver(ImageExtractObserver observer);

    public void removeObserver(ImageExtractObserver observer);

}
