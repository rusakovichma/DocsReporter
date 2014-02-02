/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.converter;

import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.springframework.stereotype.Component;

/**
 *
 * @author rusakovich
 * @param <DOCX>
 * @param <PDF>
 */
@Component
public class PdfConverterAdapter<DOCX, PDF> extends PoiConverterAdapter {

    public PdfConverterAdapter() {
        super(PdfConverter.getInstance());
    }

    @Override
    public DocFormat getTargetFormat() {
        return DocFormat.PDF;
    }

}
