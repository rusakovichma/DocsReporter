/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.converter;

import java.io.IOException;
import org.odftoolkit.odfdom.converter.core.IImageExtractor;
import org.odftoolkit.odfdom.converter.core.Options;
import org.odftoolkit.odfdom.converter.xhtml.XHTMLConverter;
import org.odftoolkit.odfdom.converter.xhtml.XHTMLOptions;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 *
 * @author rusakovich
 */
public abstract class OdfXhtmlConverterAdapter<ODT, PDF> extends OdfConverterAdapter
        implements InitializingBean {

    private XHTMLOptions options;
    private ThreadLocal<ImageExtractor> extractors;

    public OdfXhtmlConverterAdapter() {
        super(XHTMLConverter.getInstance());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        options = XHTMLOptions.create();
        extractors = new ThreadLocal<ImageExtractor>();
    }

    @Override
    public DocFormat getTargetFormat() {
        return DocFormat.XHTML;
    }

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

    public abstract ImageExtractor createExctractor();

    @Override
    public ImageExtractor getImageExtractor() {
        ImageExtractor extractor = extractors.get();
        extractors.remove();
        return extractor;
    }

}
