/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.converter;

import by.creepid.docsreporter.converter.images.ImageExtractObservable;
import by.creepid.docsreporter.converter.images.ImageExtractObservableImpl;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import org.junit.AfterClass;
import org.junit.Test;
import org.springframework.util.ReflectionUtils;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author rusakovich
 */
public class HtmlxConverterAdapterTest {

    private static final String inPath = "src/test/resources/DocxProjectWithVelocity.docx";
    private static final String outPath = "src/test/resources/DocxProjectWithVelocity.htm";
    private PoiXhtmlConverterAdapter converter;
    private static File outFile;

    public HtmlxConverterAdapterTest() {
        converter = new PoiXhtmlConverterAdapter() {

            @Override
            public ImageExtractObservable createImageExtractObservable() {
                return new ImageExtractObservableImpl();
            }
        };

        Method propSetMethod = ReflectionUtils.findMethod(PoiXhtmlConverterAdapter.class, "afterPropertiesSet");
        ReflectionUtils.invokeMethod(propSetMethod, converter);
    }

    @BeforeClass
    public static void setUpClass() {
        outFile = new File(outPath);
        outFile.deleteOnExit();
    }

    /**
     * Test of getTargetFormat method, of class HtmlxDocConverter.
     */
    @Test
    public void testGetTargetFormat() throws IOException {
        FileOutputStream newOut = null;

        try {
            InputStream in = new FileInputStream(new File(inPath));

            ByteArrayOutputStream out = (ByteArrayOutputStream) converter.convert(DocFormat.getFormat(outPath), in);

            newOut = new FileOutputStream(outFile);
            out.writeTo(newOut);

        } catch (Exception ex) {
            fail("Cannot convert template");
            ex.printStackTrace();
        } finally {
            if (newOut != null) {
                newOut.close();
            }
        }

        assertTrue(outFile.exists() && outFile.length() != 0l);
    }
}
