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
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rusakovich
 */
public class PdfConverterAdapterTest {

    private static final String inPath = "src/test/resources/DocxProjectWithVelocity.docx";
    private static final String outPath = "src/test/resources/DocxProjectWithVelocity.pdf";
    private DocConverterAdapter converter;

    public PdfConverterAdapterTest() {
        converter = new PoiPdfConverterAdapter();
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

    @Test
    public void testSomeMethod() {
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
