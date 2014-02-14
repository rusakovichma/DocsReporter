/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.utils;

import by.creepid.docsreporter.model.Developer;
import by.creepid.docsreporter.model.Project;
import by.creepid.docsreporter.model.Role;
import by.creepid.docsreporter.context.annotations.Image;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mirash
 */
public class FieldHelperTest {

    public FieldHelperTest() {
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
     * Test of getAnnotatedTypeArgumentFields method, of class FieldHelper.
     */
    @Test
    public void testGetAnnotatedTypeArgumentFields() {
        System.out.println("*** getAnnotatedTypeArgumentFields ****");
        Class clazz = Project.class;
        Class<? extends Annotation> annotationClass = Image.class;
        boolean recursively = true;
        Map<String, Field> result = FieldHelper.getAnnotatedTypeArgumentFields(clazz, "project", annotationClass, recursively);
//        for (Field field : result) {
//            System.out.println(field.getDeclaringClass().getSimpleName());
//            System.out.println(field.getName());
//        }
    }

    @Test
    public void testGetAnnotatedDeclaredFields() {
        System.out.println("getAnnotatedDeclaredFields");
        Class clazz = Project.class;
        String root = "project";
        Class<? extends Annotation> annotationClass = Image.class;
        boolean recursively = true;
        Map<String, Field> result = FieldHelper.getAnnotatedDeclaredFields(clazz, root, annotationClass, recursively);
        Set<String> str = result.keySet();
//        for (String string : str) {
//            System.out.println(string);
//        }
    }

    /**
     * Test of getFieldHierarchy method, of class FieldHelper.
     */
    @Test
    public void testGetFieldHierarchy() {
        System.out.println("***** getFieldHierarchy *******");
        Class clazz = Project.class;
        String prefix = "project";
        List<String> result = FieldHelper.getFieldHierarchy(clazz, prefix);
        for (String string : result) {
//            System.out.println(string);
        }
    }

    /**
     * Test of getFieldPath method, of class FieldHelper.
     */
    @Test
    public void testGetFieldPath_3args() {
        System.out.println("getFieldPath");
        Class fieldClass = Role.class;
        Class rootClass = Project.class;
        String prefix = "project";
        List<String> result = FieldHelper.getFieldPath(fieldClass, rootClass, prefix);
        assertEquals(1, result.size());
        assertEquals("project.Developers.Roles", result.get(0));

        fieldClass = Developer.class;
        rootClass = Project.class;
        prefix = "project";
        result = FieldHelper.getFieldPath(fieldClass, rootClass, prefix);
        assertEquals(1, result.size());
        assertEquals("project.Developers", result.get(0));

    }

    /**
     * Test of getFieldsOfClass method, of class FieldHelper.
     */
    @Test
    public void testGetFieldsOfClass() {
        System.out.println("getFieldsOfClass");
        Class target = Collection.class;
        Class rootClass = Project.class;
        String rootName = "project";
        boolean recursively = true;
        Map<String, Field> result = FieldHelper.getFieldsOfClass(target, rootClass, rootName, recursively);

        for (Map.Entry<String, Field> entry : result.entrySet()) {

            ParameterizedType collectionType = (ParameterizedType) entry.getValue().getGenericType();
            Class<?> actualTypeArg = (Class<?>) collectionType.getActualTypeArguments()[0];

            List<String> hier = FieldHelper.getFieldHierarchy(actualTypeArg, entry.getKey());
            for (String string : hier) {
                System.out.println(string);
            }
        }
    }

}
