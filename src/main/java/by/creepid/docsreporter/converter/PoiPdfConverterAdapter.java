/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.converter;

import org.apache.poi.xwpf.converter.core.Options;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 *
 * @author rusakovich
 * @param <DOCX>
 * @param <PDF>
 */
@Component
public class PoiPdfConverterAdapter<DOCX, PDF> extends PoiConverterAdapter
        implements InitializingBean, PdfConverterAdapterOptions {

    private String encoding = DEFAULT_ENCODING;
    private PdfOptions options;

    public PoiPdfConverterAdapter() {
        super(PdfConverter.getInstance());
    }

    @Override
    public DocFormat getTargetFormat() {
        return DocFormat.PDF;
    }

    @Override
    public ImageExtractor getImageExtractor() {
        return null;
    }

    @Override
    Options getOptions() {
        return options;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        options = PdfOptions.create();
        options.fontEncoding(encoding);
    }

    @Override
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

}
