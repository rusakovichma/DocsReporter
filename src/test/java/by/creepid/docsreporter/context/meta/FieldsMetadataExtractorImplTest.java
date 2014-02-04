/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.context.meta;

import by.creepid.docsreporter.DeveloperWithPhoto;
import by.creepid.docsreporter.Project;
import fr.opensagres.xdocreport.template.formatter.FieldMetadata;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
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

    /**
     * Test of fillMetadata method, of class FieldsMetadataExtractorImpl.
     */
    @Test
    public void testFillMetadata() {
        FieldsMetadata metadataToFill = new FieldsMetadata();
        Class modelClass = Project.class;
        String modelName = "project";
        Map<String, Class<?>> iteratorNames = new HashMap<String, Class<?>>();
        iteratorNames.put("dev", DeveloperWithPhoto.class);
        FieldsMetadataExtractor instance = new FieldsMetadataExtractorImpl();
        instance.fillMetadata(metadataToFill, modelClass, modelName, iteratorNames);
        Collection<FieldMetadata> metas = metadataToFill.getFieldsAsImage();
        for (FieldMetadata meta : metas) {
            System.out.println(meta.getImageName() + " - " + meta.getFieldName());
        }
    }

}
