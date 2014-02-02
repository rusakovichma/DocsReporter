package by.creepid.docsreporter.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.springframework.util.ClassUtils;

public final class ClassUtil {

    private ClassUtil() {
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
