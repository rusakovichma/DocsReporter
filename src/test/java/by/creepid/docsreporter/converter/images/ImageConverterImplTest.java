/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.converter.images;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.util.ReflectionUtils;

/**
 *
 * @author rusakovich
 */
public class ImageConverterImplTest {

    private static final String imageFolder = "src/test/resources/imageSamples/";

    private static final String target = imageFolder + "CMYK_color.jpg";
    private static final String preview = imageFolder + "CMYK_color_preview.jpg";
    private static final String rgbPreview = imageFolder + "RGB_color_preview.jpg";

    private static Path path;
    private byte[] photo;

    ImageConverter instance;

    public ImageConverterImplTest() {
        instance = new ImageConverterImpl();

        try {
            Method initMethod = ImageConverterImpl.class.getDeclaredMethod("addCMYKServiceProvider");
            initMethod.setAccessible(true);
            ReflectionUtils.invokeMethod(initMethod, instance);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @BeforeClass
    public static void setUpClass() {
        path = Paths.get(target);

    }

    @AfterClass
    public static void tearDownClass() {
        path = null;
    }

    @Before
    public void setUp() {
        try {
            photo = Files.readAllBytes(path);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of isSupportedImageType method, of class ImageConverterImpl.
     */
    @Test
    public void testIsSupportedImageType() {
        System.out.println("***** isSupportedImageType ******");
        boolean expResult = true;
        boolean result = instance.isSupportedImageType(photo);
        assertEquals(expResult, result);
    }

    /**
     * Test of isPhoto method, of class PhotosUtil.
     */
    @Test
    public void testIsPhoto() {
        System.out.println("****** isPhoto ********");
        boolean expResult = true;
        boolean result = instance.isPhoto(photo);
        assertEquals(expResult, result);
    }

    /**
     * Test of convertPhotoToPreview method, of class PhotosUtil.
     */
    @Test
    public void testConvertPhotoToPreview() throws Exception {
        System.out.println("********* convertPhotoToPreview *********");
        byte[] result = instance.convertPhotoToPreview(photo, 100, 200);
        assertNotSame(null, result);
        saveToFile(result, preview);
    }

    private void saveToFile(byte[] bytes, String path)
            throws FileNotFoundException, IOException {
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(bytes);
        fos.close();
    }

    /**
     * Test of convertToRGB method, of class PhotosUtil.
     */
    @Test
    public void testConvertToRGB() throws Exception {
        System.out.println("********* convertToRGB ********");
        byte[] cmykBytes = photo;
        byte[] result = instance.convertToRGB(cmykBytes);
        assertNotSame(null, result);
        saveToFile(result, rgbPreview);
    }

    /**
     * Test of getImageForm method, of class PhotosUtil.
     */
    @Test
    public void testGetImageForm() {
        System.out.println("***** getImageForm *******");
        ImageForm form = instance.getImageForm(photo);
        assertNotSame(ImageForm.unknown, form);
        System.out.println("FORMAT: " + form);
    }
}
