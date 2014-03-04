package by.creepid.docsreporter.context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import org.springframework.core.io.Resource;

@Component
public final class DocReportFactory {

    @Autowired
    private TemplateEngineKind templateEngineKind;

    private void checkTemplate(Resource template) {
        if (template == null) {
            throw new IllegalArgumentException("Invalid templatePath");
        }

        if (!template.exists()) {
            throw new RuntimeException("Template file not exist: [" + template.getFilename() + "]");
        }

        if (templateEngineKind == null) {
            throw new IllegalStateException("Invalid TemplateEngineKind config");
        }
    }

    public IXDocReport buildReport(Resource template) {
        checkTemplate(template);

        IXDocReport report = null;

        try {
            InputStream in = new FileInputStream(template.getFile());

            report = XDocReportRegistry.getRegistry().
                    loadReport(in, templateEngineKind);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (XDocReportException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return report;

    }

    public void setTemplateEngineKind(TemplateEngineKind templateEngineKind) {
        this.templateEngineKind = templateEngineKind;
    }

    public TemplateEngineKind getTemplateEngineKind() {
        return templateEngineKind;
    }

}
