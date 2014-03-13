/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.converter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.springframework.util.ReflectionUtils;

/**
 *
 * @author rusakovich
 */
public class PdfConverterAdapterTest {

    private static final String inPath = "src/test/resources/DocxProjectWithVelocity.docx";
    private static final String outPath = "src/test/resources/DocxProjectWithVelocity.pdf";
    private DocConverterAdapter converter;
    private static File outFile;

    public PdfConverterAdapterTest() {
        converter = new PoiPdfConverterAdapter();

        Method propSetMethod = ReflectionUtils.findMethod(PoiPdfConverterAdapter.class, "afterPropertiesSet");
        ReflectionUtils.invokeMethod(propSetMethod, converter);
    }

    @BeforeClass
    public static void setUpClass() {
        outFile = new File(outPath);
        outFile.deleteOnExit();
    }

    @Test
    public void testSomeMethod() throws IOException {
        FileOutputStream newOut = null;

        try {
            InputStream in = new FileInputStream(new File(inPath));
            ByteArrayOutputStream out = (ByteArrayOutputStream) converter.convert(DocFormat.getFormat(outPath), in);

            newOut = new FileOutputStream(new File(outPath));
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
