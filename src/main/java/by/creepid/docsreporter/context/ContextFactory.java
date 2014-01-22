/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.context;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.template.IContext;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.aop.Advisor;

import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

/**
 *
 * @author mirash
 */
public abstract class ContextFactory {

    private IContext getProxy(IContext context) {
        Pointcut pc = new ContextMatcherPointcut();
        ContextAdvice advice = new ContextAdvice(context, getContextProcessor());
        Advisor advisor = new DefaultPointcutAdvisor(pc, advice);

        ProxyFactory pf = new ProxyFactory();

        //for interfaces
        pf.setOptimize(true);

        pf.addAdvisor(advisor);
        pf.setTarget(context);

        //advise will not be changed
        pf.setFrozen(true);

        return (IContext) pf.getProxy();
    }

    public IContext buildContext(IXDocReport docReport) {
        IContext context = null;
        try {

            context = getProxy(docReport.createContext());
        } catch (XDocReportException ex) {
            Logger.getLogger(ContextFactory.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

        return context;
    }

    public abstract ContextProcessor getContextProcessor();
}
