/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.context;

import by.creepid.docsreporter.utils.ClassUtil;
import fr.opensagres.xdocreport.template.IContext;
import java.lang.reflect.Method;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.ReflectionUtils;

/**
 *
 * @author rusakovich
 */
public class ContextAdvice implements MethodInterceptor {

    private static final Class<?> TARGET_CLASS = IContext.class;
    private static final Method PUT_METHOD;
    private static final Method GET_METHOD;

    static {
        PUT_METHOD = ReflectionUtils.findMethod(TARGET_CLASS, "put", String.class, Object.class);
        GET_METHOD = ReflectionUtils.findMethod(TARGET_CLASS, "get", String.class);
    }

    private final ContextProcessor processor;

    public ContextAdvice(IContext context, ContextProcessor processor) {
        this.processor = processor;
        processor.setContext(context);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (!(invocation.getThis() instanceof IContext)) {
            throw new IllegalArgumentException("Target must be IContext instance");
        }

        Object[] args = invocation.getArguments();
        Method invoked = invocation.getMethod();

        return ClassUtil.isMethodsEquals(GET_METHOD, invoked)
                ? processor.get((String) args[0])
                : processor.put((String) args[0], args[1]);

    }
    
}
