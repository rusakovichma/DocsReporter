/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.converter;

import java.io.IOException;
import org.apache.poi.xwpf.converter.core.IImageExtractor;
import org.apache.poi.xwpf.converter.core.Options;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.springframework.beans.factory.DisposableBean;
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
        implements InitializingBean{

    private XHTMLOptions options;
    private ThreadLocal<ImageExtractor> extractors;

    public PoiXhtmlConverterAdapter() {
        super(XHTMLConverter.getInstance());
    }

    @Override
    public DocFormat getTargetFormat() {
        return DocFormat.XHTML;
    }

    @Override
    public ImageExtractor getImageExtractor() {
        ImageExtractor extractor = extractors.get();
        extractors.remove();
        return extractor;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        options = XHTMLOptions.create();
        extractors = new ThreadLocal<ImageExtractor>();
    }

    public abstract ImageExtractor createExctractor();

    @Override
    Options getOptions() {
        final ImageExtractor extractor = createExctractor();

        options.setExtractor(new IImageExtractor() {

            @Override
            public void extract(String string, byte[] bytes)
                    throws IOException {
                extractor.addImage(string, bytes);
            }
        });

        extractors.set(extractor);

        return options;
    }

}
