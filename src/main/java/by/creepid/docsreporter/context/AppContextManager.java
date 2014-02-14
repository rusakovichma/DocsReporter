/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 *
 * @author rusakovich
 */
@Component
public class AppContextManager implements ApplicationContextAware {

    private static volatile ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) {
        AppContextManager.context = context;
    }

    public static <T> T getbean(Class<T> requiredType) {
        return context.getBean(requiredType);
    }

}
