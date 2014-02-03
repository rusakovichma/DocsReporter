/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.context.meta;

import by.creepid.docsreporter.Project;
import by.creepid.docsreporter.context.annotations.ImageField;
import by.creepid.docsreporter.utils.ClassUtil;
import fr.opensagres.xdocreport.template.formatter.FieldMetadata;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import java.lang.reflect.Field;
import java.util.Collection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author mirash
 */
public class FieldsMetadataExtractorImplTest {

    private FieldsMetadataExtractorImpl instance;

    public FieldsMetadataExtractorImplTest() {
        instance = new FieldsMetadataExtractorImpl();
    }

    @BeforeClass
    public static void setUpClass() {
//        Field[] f = ClassUtil.getAnnotatedDeclaredFields(Project.class, ImageField.class, true);
//        for (Field field : f) {
//            System.out.println(field.getName());
//            System.out.println(field.getDeclaringClass());
//        }
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
     * Test of getMetadata method, of class FieldsMetadataExtractorImpl.
     */
    @Test
    public void testGetMetadata() {
        System.out.println("***** getMetadata *******");
        Class<?> targetClass = Project.class;
//        FieldsMetadata result = instance.fillMetadata(new FieldsMetadata(), targetClass);
//        Collection<FieldMetadata> fields = result.getFieldsAsImage();
//        for (FieldMetadata fieldMetadata : fields) {
//            System.out.println(fieldMetadata.getFieldName());
//        }
    }
}
