package by.creepid.docsreporter;

import by.creepid.docsreporter.context.ContextAdvice;
import by.creepid.docsreporter.context.ContextMatcherPointcut;
import by.creepid.docsreporter.context.ContextProcessor;
import by.creepid.docsreporter.context.DocReportFactory;
import by.creepid.docsreporter.converter.DocConverter;
import by.creepid.docsreporter.converter.DocFormat;
import static by.creepid.docsreporter.converter.DocFormat.*;
import by.creepid.docsreporter.converter.PoiDocConverter;
import fr.opensagres.xdocreport.core.XDocReportException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.springframework.stereotype.Service;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.template.IContext;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public abstract class DocReportBuilder implements ReportBuilder {

    @Autowired
    private ContextProcessor contextProcessor;
    @Autowired
    private DocReportFactory docReportFactory;
    
    @Resource(name = "docConverters")
    private List<PoiDocConverter> docConverters;
    
    private IContext context;
    private IXDocReport docReport;
    
    private String templatePath;
    private DocFormat templateExt;

    public void initContext() {
        docReport = docReportFactory.buildReport(templatePath);
        try {

            context = getProxy(docReport.createContext());
        } catch (XDocReportException ex) {
            Logger.getLogger(DocReportBuilder.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

        templateExt = getFormat(templatePath);
        if (templateExt == UNSUPPORTED) {
            throw new IllegalStateException("Given template format is not supported!");
        }
    }

    private IContext getProxy(IContext context) {
        Pointcut pc = new ContextMatcherPointcut();
        ContextAdvice advice = new ContextAdvice(context, contextProcessor);
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

    private void clearSAXDriverProperty() {
        if (System.getProperty("org.xml.sax.driver") != null) {
            System.clearProperty("org.xml.sax.driver");
        }
    }

    private DocConverter findConverter(DocFormat format) {
        if (docConverters == null) {
            throw new IllegalStateException("No document converters found!");
        }

        for (DocConverter docConverter : docConverters) {
            if (docConverter != null && docConverter.getTargetFormat() == format) {
                return docConverter;
            }
        }

        throw new IllegalStateException("No document converters found!");
    }

    @Override
    public void generateReport(String reportOutputPath, String modelName,
            Object model) throws Exception {
        OutputStream out = null;
        try {
            DocFormat format = DocFormat.getFormat(reportOutputPath);
            if (format == UNSUPPORTED) {
                throw new IllegalArgumentException("Given format is not supported!");
            }

            String output = (format == templateExt)
                    ? reportOutputPath
                    : templateExt.changeExt(reportOutputPath);

            context.put(modelName, model);

            out = new FileOutputStream(
                    new File(output));

            clearSAXDriverProperty();

            docReport.process(context, out);

            if (format != templateExt) {
                DocConverter converter = findConverter(format);
                converter.convert(output, reportOutputPath);
            }

        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public void setDocReportFactory(DocReportFactory docReportFactory) {
        this.docReportFactory = docReportFactory;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public void setDocConverters(List<PoiDocConverter> docConverters) {
        this.docConverters = docConverters;
    }

    public abstract ContextProcessor getContextProcessor();
}
