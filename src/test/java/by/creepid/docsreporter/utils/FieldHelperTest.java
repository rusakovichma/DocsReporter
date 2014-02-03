/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.utils;

import by.creepid.docsreporter.Project;
import by.creepid.docsreporter.context.annotations.ImageField;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
        Class fieldType = Collection.class;
        Class<? extends Annotation> annotationClass = ImageField.class;
        boolean recursively = true;
        Field[] result = FieldHelper.getAnnotatedTypeArgumentFields(clazz, fieldType, annotationClass, recursively);
        for (Field field : result) {
            System.out.println(field.getDeclaringClass().getSimpleName());
            System.out.println(field.getName());
        }
    }

    @Test
    public void testGetAnnotatedDeclaredFields() {
        System.out.println("getAnnotatedDeclaredFields");
        Class clazz = Project.class;
        String root = "project";
        Class<? extends Annotation> annotationClass = ImageField.class;
        boolean recursively = true;
        Map<String, Field> result = FieldHelper.getAnnotatedDeclaredFields(clazz, root, annotationClass, recursively);
        Set<String> str = result.keySet();
        for (String string : str) {
            System.out.println(string);
        }
    }

    

    
}
