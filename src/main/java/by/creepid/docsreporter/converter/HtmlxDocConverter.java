/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.converter;

import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.springframework.stereotype.Component;

/**
 *
 * @author rusakovich
 * @param <HTMLX>
 * @param <HTMLXOptions>
 */
@Component
public class HtmlxDocConverter<DOCX, HTMLX> extends PoiDocConverter {

    public HtmlxDocConverter() {
        super(XHTMLConverter.getInstance());
    }

    @Override
    public DocFormat getTargetFormat() {
        return DocFormat.XHTML;
    }

}
