package by.creepid.docsreporter.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public final class ClassUtil {

    private ClassUtil() {
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
    public static Field[] getAnnotatedDeclaredFields(Class clazz,
            Class<? extends Annotation> annotationClass,
            boolean recursively) {
        Field[] allFields = getDeclaredFields(clazz, recursively);
        List<Field> annotatedFields = new LinkedList<Field>();

        for (Field field : allFields) {
            if (field.isAnnotationPresent(annotationClass)) {
                annotatedFields.add(field);
            }
        }

        return annotatedFields.toArray(new Field[annotatedFields.size()]);
    }

    public static boolean isImplInterface(Class<?> target, Class<?> inter) {
        Class<?>[] interfaces = target.getInterfaces();

        for (Class<?> curr : interfaces) {
            if (curr == inter) {
                return true;
            }
        }

        return false;
    }

    public static boolean isMethodsEquals(Method target, Method current) {
        if (target == current) {
            return true;
        }

        if (current == null || target == null) {
            return false;
        }

        String name = target.getName();
        if (!name.equals(current.getName())) {
            return false;
        }

        Class<?> returnType = target.getReturnType();
        if (returnType != current.getReturnType()) {
            return false;
        }

        Class<?>[] targetParams = target.getParameterTypes();
        Class<?>[] currParams = current.getParameterTypes();
        if (targetParams.length != currParams.length) {
            return false;
        }

        boolean paramsResult = true;
        for (int i = 0; i < targetParams.length; i++) {
            paramsResult = (targetParams[i] == currParams[i]);
            if (!paramsResult) {
                break;
            }
        }

        return paramsResult;
    }

}
