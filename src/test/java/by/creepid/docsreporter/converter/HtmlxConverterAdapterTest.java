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
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author rusakovich
 */
public class HtmlxConverterAdapterTest {

    private static final String inPath = "src/test/resources/DocxProjectWithVelocity.docx";
    private static final String outPath = "src/test/resources/DocxProjectWithVelocity.htm";
    private DocConverterAdapter converter;

    public HtmlxConverterAdapterTest() {
        converter = new PoiXhtmlConverterAdapter() {

            @Override
            public ImageExtractObservable createImageExtractObservable() {
                return new ImageExtractObservableImpl();
            }
        };
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getTargetFormat method, of class HtmlxDocConverter.
     */
    @Test
    public void testGetTargetFormat() {
        System.out.println(converter.getSourceFormat());
        System.out.println(converter.getTargetFormat());
        try {
            InputStream in = new FileInputStream(new File(inPath));
            ByteArrayOutputStream out = (ByteArrayOutputStream) converter.convert(DocFormat.getFormat(outPath), in);

            FileOutputStream newOut = new FileOutputStream(new File(outPath));
            out.writeTo(newOut);

        } catch (Exception ex) {
            Logger.getLogger(PdfConverterAdapterTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
