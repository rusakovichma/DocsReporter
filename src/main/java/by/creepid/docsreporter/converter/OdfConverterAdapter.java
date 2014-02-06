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
import org.odftoolkit.odfdom.converter.core.IODFConverter;
import org.odftoolkit.odfdom.converter.core.Options;
import org.odftoolkit.odfdom.converter.pdf.PdfOptions;
import org.odftoolkit.odfdom.doc.OdfTextDocument;

/**
 *
 * @author rusakovich
 */
public abstract class OdfConverterAdapter<S extends DocFormat, E extends DocFormat>
        implements DocConverterAdapter {

    protected final IODFConverter converter;
    protected E format;

    OdfConverterAdapter(IODFConverter converter) {
        this.converter = converter;
    }

    private OdfTextDocument createDocument(InputStream in)
            throws Exception {
        return OdfTextDocument.loadDocument(in);
    }

    public OutputStream convert(DocFormat targetFormat, InputStream in)
            throws IOException {
        if (targetFormat != getTargetFormat()) {
            throw new IllegalArgumentException(
                    "Target must have " + getTargetFormat().getExts()[0] + " extension!");
        }
        OdfTextDocument doc = null;

        try {
            doc = createDocument(in);
        } catch (Exception ex) {
            throw new IOException(ex);
        }

        OutputStream out = new ByteArrayOutputStream();

        synchronized (converter) {
            converter.convert(doc, out, getOptions());
        }

        return out;
    }

    @Override
    public abstract E getTargetFormat();

    abstract Options getOptions();

    @Override
    public DocFormat getSourceFormat() {
        return DocFormat.ODT;
    }

}
