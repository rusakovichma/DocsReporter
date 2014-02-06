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
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.ReflectionUtils;

/**
 *
 * @author rusakovich
 */
public class ContextAdvice implements MethodInterceptor {

    private static final String PUT_METHOD_NAME = "put";
    private static final String GET_METHOD_NAME = "get";
    private static final String PUT_MAP_METHOD_NAME = "putMap";
    private static final String GET_MAP_METHOD_NAME = "getContextMap";

    private static final Class<?> TARGET_CLASS = IContext.class;
    private static final List<Method> targetMethods = new CopyOnWriteArrayList();
    private static List<String> templateSystemPrefixes
            = Arrays.asList("___", "velocity", "freemarker", "list", "foreach");

    static {
        targetMethods.add(ReflectionUtils.findMethod(TARGET_CLASS, PUT_METHOD_NAME, String.class, Object.class));
        targetMethods.add(ReflectionUtils.findMethod(TARGET_CLASS, GET_METHOD_NAME, String.class));
        targetMethods.add(ReflectionUtils.findMethod(TARGET_CLASS, PUT_MAP_METHOD_NAME, Map.class));
        targetMethods.add(ReflectionUtils.findMethod(TARGET_CLASS, GET_MAP_METHOD_NAME, Map.class));

        templateSystemPrefixes = Collections.unmodifiableList(templateSystemPrefixes);
    }
    private final ContextProcessor processor;

    public ContextAdvice(IContext context, ContextProcessor processor) {
        this.processor = processor;
        processor.setContext(context);
    }

    protected boolean isTemplateSystemName(String str) {

        for (String prefix : templateSystemPrefixes) {
            if (str.startsWith(prefix)) {
                return true;
            }
        }

        return false;
    }

    private boolean ckeckInvokedMethod(Method invoked) {
        boolean result = false;
        for (Method method : targetMethods) {

            result = ClassUtil.isMethodsEquals(method, invoked);

            if (result) {
                break;
            }
        }
        return result;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (!(invocation.getThis() instanceof IContext)) {
            throw new IllegalArgumentException("Target must be IContext instance");
        }

        Object[] args = invocation.getArguments();
        String str = null;
        if (args[0] instanceof String) {
            str = (String) args[0];
            if (isTemplateSystemName(str)) {
                return invocation.proceed();
            }
        }

        Method invoked = invocation.getMethod();
        if (ckeckInvokedMethod(invoked)) {
            switch (invoked.getName()) {
                case PUT_METHOD_NAME:
                    return processor.put(str, args[1]);
                case GET_METHOD_NAME:
                    return processor.get(str);
                case PUT_MAP_METHOD_NAME:
                    processor.putMap((Map) args[0]);
                    return null;
                case GET_MAP_METHOD_NAME:
                    return processor.getContextMap();
                default:
                    return invocation.proceed();
            }
        } else {
            return invocation.proceed();
        }

    }

}
