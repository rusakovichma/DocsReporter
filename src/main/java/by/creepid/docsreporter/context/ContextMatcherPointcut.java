/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.context;

import by.creepid.docsreporter.utils.ClassUtil;
import fr.opensagres.xdocreport.template.IContext;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.util.ReflectionUtils;

/**
 *
 * @author rusakovich
 */
public class ContextMatcherPointcut extends StaticMethodMatcherPointcut {

    private static List<Method> contextMethods;

    static {
        contextMethods = Arrays.asList(
                ReflectionUtils.getAllDeclaredMethods(IContext.class));

        contextMethods = Collections.unmodifiableList(contextMethods);
    }

    private static class ContextClassFilter implements ClassFilter {

        public boolean matches(Class<?> type) {
            return ClassUtil.isImplInterface(type, IContext.class);
        }

    }

    private boolean isMethodContain(Method target) {
        boolean result = false;

        for (Method current : contextMethods) {
            result = ClassUtil.isMethodsEquals(target, current);
            if (result) {
                return result;
            }
        }

        return result;
    }

    public boolean matches(Method method, Class<?> type) {
        return isMethodContain(method);
    }

    @Override
    public ClassFilter getClassFilter() {
        return new ContextClassFilter();
    }

}
