/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.FieldFilter;

/**
 *
 * @author mirash
 */
public class FieldHelper {

    public static List<String> getFieldHierarchy(final Class<?> clazz, final String prefix) {
        final List<String> rootHierarchy = new LinkedList<String>();

        ReflectionUtils.doWithFields(clazz,
                new FieldCallback() {

                    @Override
                    public void doWith(final Field field)
                    throws IllegalArgumentException, IllegalAccessException {
                        field.setAccessible(true);
                        String fieldPath = getFieldPath(prefix, field.getName());
                        rootHierarchy.add(fieldPath);

                        if (!SimpleTypes.isSimple(field.getType())
                        && !ClassUtils.isPrimitiveArray(field.getType())
                        && !Collection.class.isAssignableFrom(field.getType())) {

                            List<String> fieldHierarchy = getFieldHierarchy(field.getType(), fieldPath);
                            rootHierarchy.addAll(fieldHierarchy);
                        }

                        if (Collection.class.isAssignableFrom(field.getType())
                        && (field.getGenericType() != null)
                        && (field.getGenericType() instanceof ParameterizedType)) {
                            ParameterizedType collectionType = (ParameterizedType) field.getGenericType();
                            Class<?> actualTypeArg = (Class<?>) collectionType.getActualTypeArguments()[0];

                            List<String> genericHierarchy = getFieldHierarchy(actualTypeArg, getFieldPath(prefix, field.getName()));
                            rootHierarchy.addAll(genericHierarchy);
                        }

                    }
                },
                new FieldFilter() {

                    @Override
                    public boolean matches(final Field field) {
                        final int modifiers = field.getModifiers();
                        // no static fields please
                        return !Modifier.isStatic(modifiers)
                        && !Modifier.isTransient(modifiers)
                        && !Modifier.isAbstract(modifiers);
                    }
                });

        return rootHierarchy;
    }

    /**
     * Retrieving fields list of specified class If recursively is true,
     * retrieving fields from all class hierarchy
     *
     * @param clazz where fields are searching
     * @param recursively param
     * @return list of fields
     */
    public static Field[] getDeclaredFields(Class clazz, boolean recursively) {
        List<Field> fields = new LinkedList<Field>();
        Field[] declaredFields = clazz.getDeclaredFields();
        Collections.addAll(fields, declaredFields);

        Class superClass = clazz.getSuperclass();

        if (superClass != null && recursively) {
            Field[] declaredFieldsOfSuper = getDeclaredFields(superClass, recursively);
            if (declaredFieldsOfSuper.length > 0) {
                Collections.addAll(fields, declaredFieldsOfSuper);
            }
        }

        return fields.toArray(new Field[fields.size()]);
    }

    public static Field[] getAnnotatedTypeArgumentFields(Class clazz, Class fieldType, Class<? extends Annotation> annotationClass, boolean recursively) {
        Field[] allFields = getDeclaredFields(clazz, recursively);
        List<Field> annotatedFields = new LinkedList<Field>();

        for (Field field : allFields) {

            if (fieldType.isAssignableFrom(field.getType())
                    && field.getGenericType() != null
                    && (field.getGenericType() instanceof ParameterizedType)) {

                ParameterizedType collectionType = (ParameterizedType) field.getGenericType();
                Class<?> actualTypeArg = (Class<?>) collectionType.getActualTypeArguments()[0];

                if (recursively) {
                    Field[] fields = getAnnotatedDeclaredFields(
                            actualTypeArg, null, annotationClass, true).
                            values().toArray(new Field[]{});

                    List< Field> fieldList = Arrays.asList(fields);
                    annotatedFields.addAll(fieldList);
                }

            }
        }

        return annotatedFields.toArray(new Field[annotatedFields.size()]);
    }

    /**
     * Retrieving fields list of specified class and which are annotated by
     * incoming annotation class If recursively is true, retrieving fields from
     * all class hierarchy
     *
     * @param clazz - where fields are searching
     * @param annotationClass - specified annotation class
     * @param recursively param
     * @return list of annotated fields
     */
    public static Map<String, Field> getAnnotatedDeclaredFields(Class clazz, String root,
            Class<? extends Annotation> annotationClass,
            boolean recursively) {
        Field[] allFields = getDeclaredFields(clazz, recursively);
        Map<String, Field> annotatedFields = new HashMap<String, Field>();

        for (Field field : allFields) {
            if (field.isAnnotationPresent(annotationClass)) {

                annotatedFields.put(
                        getFieldPath(root, field.getName()),
                        field);
            }

            if (recursively
                    && !SimpleTypes.isSimple(field.getType())
                    && !ClassUtils.isPrimitiveArray(field.getType())
                    && !Collection.class.isAssignableFrom(field.getType())) {

                Map<String, Field> fieldMap = getAnnotatedDeclaredFields(
                        field.getType(),
                        getFieldPath(root, field.getName()),
                        annotationClass, true);

                annotatedFields.putAll(fieldMap);
            }
        }

        return annotatedFields;
    }

    public static String getFieldPath(String root, String fieldName) {
        if (root == null || root.isEmpty()) {
            return StringsUtil.setFirstCharLower(fieldName);
        }

        StringBuilder contextField = new StringBuilder(
                StringsUtil.setFirstCharLower(root))
                .append(".")
                .append(StringsUtil.setFirstCharUpper(fieldName));

        return contextField.toString();
    }

    /**
     * get a Field including superclasses
     *
     * @param c
     * @param fieldName
     * @return
     */
    public Field getField(Class<?> c, String fieldName) {
        Field result = null;
        try {
            result = c.getDeclaredField(fieldName);
        } catch (NoSuchFieldException nsfe) {
            Class<?> sc = c.getSuperclass();
            result = getField(sc, fieldName);
        }
        return result;
    }

    /**
     * set a field Value by name
     *
     * @param fieldName
     * @param Value
     * @throws Exception
     */
    public void setFieldValue(Object target, String fieldName, Object value) throws Exception {
        Class<? extends Object> c = target.getClass();
        Field field = getField(c, fieldName);
        field.setAccessible(true);
        // beware of ...
        // http://docs.oracle.com/javase/tutorial/reflect/member/fieldTrouble.html
        field.set(this, value);
    }

    /**
     * get a field Value by name
     *
     * @param fieldName
     * @return
     * @throws Exception
     */
    public Object getFieldValue(Object target, String fieldName) throws Exception {
        Class<? extends Object> c = target.getClass();
        Field field = getField(c, fieldName);
        field.setAccessible(true);
        Object result = field.get(target);
        return result;
    }
}
