/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.converter;

import by.creepid.docsreporter.converter.images.ImageExtractObserver;
import org.odftoolkit.odfdom.converter.core.Options;
import org.odftoolkit.odfdom.converter.pdf.PdfConverter;
import org.odftoolkit.odfdom.converter.pdf.PdfOptions;
import org.springframework.beans.factory.InitializingBean;

/**
 *
 * @author rusakovich
 */
public class OdfPdfConverterAdapter<ODT, PDF> extends OdfConverterAdapter
        implements InitializingBean, PdfConverterAdapterOptions {

    private String encoding = DEFAULT_ENCODING;
    private PdfOptions options;

    public OdfPdfConverterAdapter() {
        super(PdfConverter.getInstance());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        options = PdfOptions.create();
        options.fontEncoding(encoding);
    }

    @Override
    public DocFormat getTargetFormat() {
        return DocFormat.PDF;
    }

    @Override
    Options getOptions() {
        return options;
    }

    @Override
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public void addImageExtractObserver(ImageExtractObserver observer) {
    }

    @Override
    public void removeImageExtractObserver(ImageExtractObserver observer) {
    }

}
