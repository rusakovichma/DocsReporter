/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.converter;

import by.creepid.docsreporter.converter.images.ImageExtractObserver;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author rusakovich
 * @param <S>
 * @param <E>
 */
public interface DocConverterAdapter<S extends DocFormat, E extends DocFormat> {

    public OutputStream convert(DocFormat sourceFormat, InputStream in)
            throws Exception;

    public E getTargetFormat();

    public S getSourceFormat();

    public void addImageExtractObserver(ImageExtractObserver observer);

    public void removeImageExtractObserver(ImageExtractObserver observer);

}
