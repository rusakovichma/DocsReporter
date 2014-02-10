/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.converter;

import by.creepid.docsreporter.converter.images.ImageExtractObservable;
import by.creepid.docsreporter.converter.images.ImageExtractObserver;
import java.io.IOException;
import org.apache.poi.xwpf.converter.core.IImageExtractor;
import org.apache.poi.xwpf.converter.core.Options;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 *
 * @author rusakovich
 * @param <HTMLX>
 * @param <HTMLXOptions>
 */
@Component
public abstract class PoiXhtmlConverterAdapter<DOCX, HTMLX> extends PoiConverterAdapter
        implements InitializingBean {

    private XHTMLOptions options;
    private ThreadLocal<ImageExtractObservable> observables;

    public PoiXhtmlConverterAdapter() {
        super(XHTMLConverter.getInstance());
    }

    @Override
    public DocFormat getTargetFormat() {
        return DocFormat.XHTML;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        options = XHTMLOptions.create();
        observables = new ThreadLocal<ImageExtractObservable>();
    }

    @Override
    Options getOptions() {
        final ImageExtractObservable observable = getImageExtractObservable();

        options.setExtractor(new IImageExtractor() {

            @Override
            public void extract(String string, byte[] bytes)
                    throws IOException {
                observable.fireExtractImageEvent(bytes, string);
            }
        });

        return options;
    }

    public abstract ImageExtractObservable createImageExtractObservable();

    public ImageExtractObservable getImageExtractObservable() {
        if (observables.get() == null) {
            observables.set(createImageExtractObservable());
        }

        return observables.get();
    }

    @Override
    public void addImageExtractObserver(ImageExtractObserver observer) {
        ImageExtractObservable observable = getImageExtractObservable();
        observable.addImageExtractObserver(observer);
    }

    @Override
    public void removeImageExtractObserver(ImageExtractObserver observer) {
        ImageExtractObservable observable = getImageExtractObservable();
        observable.removeObserver(observer);
    }

}
