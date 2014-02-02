/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.converter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.poi.xwpf.converter.core.IXWPFConverter;
import org.apache.poi.xwpf.converter.core.Options;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

/**
 *
 * @author rusakovich
 */
public abstract class PoiConverterAdapter<S extends DocFormat, E extends DocFormat>
        implements DocConverterAdapter {

    protected final IXWPFConverter converter;
    protected E format;

    PoiConverterAdapter(IXWPFConverter converter) {
        this.converter = converter;
    }

    private XWPFDocument createDocument(InputStream in)
            throws IOException {
        return new XWPFDocument(in);
    }

    public OutputStream convert(DocFormat targetFormat, InputStream in, Options options)
            throws IOException {
        if (targetFormat != getTargetFormat()) {
            throw new IllegalArgumentException(
                    "Target must have " + getTargetFormat().getExts()[0] + " extension!");
        }

        XWPFDocument doc = createDocument(in);
        OutputStream out = new ByteArrayOutputStream();

        synchronized (converter) {
            converter.convert(doc, out, options);
        }

        return out;
    }

    @Override
    public OutputStream convert(DocFormat inFormat, InputStream in)
            throws IOException {
        return convert(inFormat, in, null);
    }

    @Override
    public abstract E getTargetFormat();

    @Override
    public DocFormat getSourceFormat() {
        return DocFormat.DOCX;
    }
}
