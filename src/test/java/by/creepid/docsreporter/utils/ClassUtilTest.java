/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.utils;

import by.creepid.docsreporter.Project;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
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
public class ClassUtilTest {

    public ClassUtilTest() {
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

//    /**
//     * Test of getDeclaredFields method, of class ClassUtil.
//     */
//    @Test
//    public void testGetDeclaredFields() {
//        System.out.println("getDeclaredFields");
//        Class clazz = null;
//        boolean recursively = false;
//        Field[] expResult = null;
//        Field[] result = ClassUtil.getDeclaredFields(clazz, recursively);
//        assertArrayEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getAnnotatedDeclaredFields method, of class ClassUtil.
//     */
//    @Test
//    public void testGetAnnotatedDeclaredFields() {
//        System.out.println("getAnnotatedDeclaredFields");
//        Class clazz = null;
//        Class<? extends Annotation> annotationClass = null;
//        boolean recursively = false;
//        Field[] expResult = null;
//        Field[] result = ClassUtil.getAnnotatedDeclaredFields(clazz, annotationClass, recursively);
//        assertArrayEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
    /**
     * Test of isImplInterface method, of class ClassUtil.
     */
    @Test
    public void testIsImplInterface() {
        Object obj = new Project().getDevelopers();
        System.out.println(obj instanceof Collection);
    }
}
