/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.context;

import by.creepid.docsreporter.model.DeveloperWithPhoto;
import by.creepid.docsreporter.model.Manager;
import by.creepid.docsreporter.model.Project;
import by.creepid.docsreporter.model.Role;
import by.creepid.docsreporter.converter.images.ImageConverter;
import by.creepid.docsreporter.formatter.DocTypesFormatter;
import fr.opensagres.xdocreport.document.images.ByteArrayImageProvider;
import fr.opensagres.xdocreport.template.IContext;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import static org.mockito.Mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static org.junit.Assert.*;

/**
 *
 * @author rusakovich
 */
public class DocContextProcessorTest {

    private ContextProcessor instance;

    private static final String photoFolder = "src/test/resources/photoSamples/";
    private static final String previewFolder = "src/test/resources/previewSamples/";

    private final SimpleDateFormat dateFormat;

    private final DecimalFormat decimalFormatter;

    public DocContextProcessorTest() {
        Locale locale = Locale.getDefault();
        decimalFormatter = (DecimalFormat) NumberFormat.getInstance(locale);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator(' ');
        decimalFormatter.setDecimalFormatSymbols(symbols);

        dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    }

    private static final byte[] logo;
    private static final byte[] photo1;
    private static final byte[] photo2;
    private static final byte[] photo3;

    private static final byte[] logoPreview;
    private static final byte[] photo1Preview;
    private static final byte[] photo2Preview;
    private static final byte[] photo3Preview;

    static {
        photo1 = getImage(photoFolder + "photo1.jpeg");
        photo2 = getImage(photoFolder + "photo2.jpeg");
        photo3 = getImage(photoFolder + "photo3.jpeg");
        logo = getImage(photoFolder + "logo.jpeg");

        photo1Preview = getImage(previewFolder + "photo1Preview.jpeg");
        photo2Preview = getImage(previewFolder + "photo2Preview.jpeg");
        photo3Preview = getImage(previewFolder + "photo3Preview.jpeg");
        logoPreview = getImage(previewFolder + "logoPreview.jpeg");
    }

    private Project project;

    private static byte[] getImage(String path) {
        File fi = new File(path);

        try {
            return Files.readAllBytes(fi.toPath());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        throw new RuntimeException("Cannot set the photo");
    }

    @Before
    public void setUp() {
        instance = new DocContextProcessor();
        project = createProject();
    }

    private Project createProject() {
        Project proj = new Project("project name", new Date(), new BigDecimal("123.123"));

        Manager manager = new Manager("Mike", "Green", 5);
        String string = "January 2, 1982";
        Date birthDate = null;

        try {
            birthDate = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(string);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        manager.setBirthDate(birthDate);

        proj.setManager(manager);

        List<Role> roles = new ArrayList<Role>();
        roles.add(new Role("Programmer"));

        proj.add(new DeveloperWithPhoto("ZERR", "Angelo",
                "angelo.zerr@gmail.com", birthDate, photo2, roles));

        roles.add(new Role("Architect"));
        proj.add(new DeveloperWithPhoto("Leclercq", "Pascal",
                "pascal.leclercq@gmail.com", null, photo1, roles));

        roles = new ArrayList<Role>();
        roles.add(new Role("System programmer"));
        proj.add(new DeveloperWithPhoto("Leclercq", "Pascal",
                null, birthDate, photo1, roles));

        roles = new ArrayList<Role>();
        roles.add(new Role("Web programmer"));
        proj.add(new DeveloperWithPhoto("Arnold", "Brown",
                "arnoldbrown@yahoo.com", birthDate, photo2, roles));

        roles.add(new Role("Web programmer"));
        roles.add(new Role("GUI programmer"));
        proj.add(new DeveloperWithPhoto("Jim", "Smith",
                "jimmythebest@tut.by", birthDate, photo3, roles));

        proj.setLogo(logo);

        return proj;
    }

    /**
     * Test of put method, of class DocContextProcessor.
     */
    @Test
    public void testPut() throws IOException {
        System.out.println("******* put ********");

        String string = "project";
        final Object obj = project;

        ImageConverter imageConverter = mock(ImageConverter.class);

        when(imageConverter.isImage(any(byte[].class))).thenReturn(Boolean.TRUE);
        when(imageConverter.isSupportedImageType(any(byte[].class))).thenReturn(Boolean.TRUE);

        when(imageConverter.isImage(null)).thenReturn(Boolean.FALSE);
        when(imageConverter.isSupportedImageType(null)).thenReturn(Boolean.FALSE);

        when(imageConverter.convertPhotoToPreview(logo, 200, Integer.MIN_VALUE)).thenReturn(logoPreview);
        when(imageConverter.convertPhotoToPreview(photo1, 100, Integer.MIN_VALUE)).thenReturn(photo1Preview);
        when(imageConverter.convertPhotoToPreview(photo2, 100, Integer.MIN_VALUE)).thenReturn(photo2Preview);
        when(imageConverter.convertPhotoToPreview(photo3, 100, Integer.MIN_VALUE)).thenReturn(photo3Preview);

        ReflectionTestUtils.invokeSetterMethod(instance, "setImageConverter", imageConverter);

        List<DocTypesFormatter> formatters = new ArrayList<>();

        DocTypesFormatter docDateFormatter = mock(DocTypesFormatter.class);
        when(docDateFormatter.getFromClass()).thenReturn(Date.class);
        when(docDateFormatter.getToClass()).thenReturn(String.class);
        when(docDateFormatter.format(anyObject())).thenAnswer(new Answer<String>() {

            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                Date date = (Date) invocation.getArguments()[0];
                return dateFormat.format(date);
            }
        });

        DocTypesFormatter docDecimalFormatter = mock(DocTypesFormatter.class);
        when(docDecimalFormatter.getFromClass()).thenReturn(BigDecimal.class);
        when(docDecimalFormatter.getToClass()).thenReturn(String.class);
        when(docDecimalFormatter.format(anyObject())).thenAnswer(new Answer<String>() {

            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                BigDecimal decimal = (BigDecimal) invocation.getArguments()[0];
                return (decimal != null) ? decimalFormatter.format(decimal) : null;
            }
        });

        formatters.add(docDateFormatter);
        formatters.add(docDecimalFormatter);

        ReflectionTestUtils.invokeSetterMethod(instance, "setFormatters", formatters);

        IContext context = mock(IContext.class);

        when(context.put(anyString(), anyObject())).thenAnswer(new Answer<Object>() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object arg0 = invocation.getArguments()[0];
                Object arg1 = invocation.getArguments()[1];

                assertNotNull(arg0);
                assertNotNull(arg1);

                if (arg1 instanceof ByteArrayImageProvider) {
                    ByteArrayImageProvider prov = (ByteArrayImageProvider) arg1;

                    assertTrue(prov.isValid());
                    assertNotNull(prov.getImageByteArray());
                    assertTrue(prov.getImageByteArray().length != 0);
                }

                if (arg0 instanceof String) {
                    String str = (String) arg0;

                    if (str.equals("project.Date")) {
                        String strDate = (String) arg1;
                        try {
                            dateFormat.parse(strDate);
                        } catch (ParseException ex) {
                            ex.printStackTrace();
                            fail("Cannot parse " + str + " value: " + strDate);
                        }
                    }

                    if (str.equals("project.Price")) {
                        String strDecimal = (String) arg1;
                        try {
                            decimalFormatter.parse(strDecimal);
                        } catch (ParseException ex) {
                            ex.printStackTrace();
                            fail("Cannot parse " + str + " value: " + strDecimal);
                        }
                    }

                }

                return obj;
            }
        });

        instance.setContext(context);
        instance.put(string, obj);
    }

    /**
     * Test of get method, of class DocContextProcessor.
     */
    @Test
    public void testGet() {
        System.out.println("*********** get ***********");
        final Object obj = new Object();
        final String string = "test";

        IContext context = mock(IContext.class);

        when(context.get(string)).thenAnswer(new Answer<Object>() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return obj;
            }
        });

        instance.setContext(context);

        assertEquals(obj, instance.get(string));
    }

}
