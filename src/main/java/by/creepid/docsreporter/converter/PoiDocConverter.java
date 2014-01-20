/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.converter;

import by.creepid.docsreporter.utils.FileUtils;
import java.io.FileInputStream;
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
public abstract class PoiDocConverter<S extends DocFormat, E extends DocFormat>
        implements DocConverter {

    protected final IXWPFConverter converter;
    protected E format;

    PoiDocConverter(IXWPFConverter converter) {
        this.converter = converter;
    }

    private XWPFDocument createDocument(String docPath)
            throws IOException {
        InputStream in = new FileInputStream(docPath);
        return new XWPFDocument(in);
    }

    public void convert(String sourcePath, String targetPath, Options options)
            throws IOException {
        if (DocFormat.getFormat(sourcePath) != getSourceFormat()) {
            throw new IllegalArgumentException(
                    "Source must have " + getSourceFormat().getExts()[0] + " extension!");
        }
        
        if (DocFormat.getFormat(targetPath) != getTargetFormat()) {
            throw new IllegalArgumentException(
                    "Target must have " + getTargetFormat().getExtsString(" or ") + " extension!");
        }

        XWPFDocument doc = createDocument(sourcePath);
        OutputStream out = FileUtils.getOutputStream(targetPath);

        synchronized (converter) {
            converter.convert(doc, out, options);
        }
    }

    @Override
    public void convert(String sourcePath, String targetPath)
            throws IOException {
        convert(sourcePath, targetPath, null);
    }

    @Override
    public abstract E getTargetFormat();

    @Override
    public DocFormat getSourceFormat() {
        return DocFormat.DOCX;
    }
}
