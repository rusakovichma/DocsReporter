package by.creepid.docsreporter.utils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.springframework.util.ClassUtils;

public final class SimpleTypes {

    private static Set<Class<?>> types = new HashSet<Class<?>>();

    static {
        types.add(Boolean.class);
        types.add(String.class);
        types.add(Character.class);
        types.add(Byte.class);
        types.add(Short.class);
        types.add(Integer.class);
        types.add(Long.class);
        types.add(Float.class);
        types.add(Double.class);
        types.add(Void.class);
        types.add(BigDecimal.class);
        types.add(Date.class);

        types = Collections.unmodifiableSet(types);
    }

    private SimpleTypes() {
    }

    public static boolean isSimple(Class<?> clazz) {
        return ClassUtils.isPrimitiveOrWrapper(clazz)
                || types.contains(clazz)
                || Enum.class.isAssignableFrom(clazz);
    }

    public static Set<Class<?>> getTypes() {
        return types;
    }
}
