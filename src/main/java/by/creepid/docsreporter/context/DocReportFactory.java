package by.creepid.docsreporter.context;

import by.creepid.docsreporter.utils.FileUtil;
import java.io.File;
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

@Component
public final class DocReportFactory {

    @Autowired
    private TemplateEngineKind templateEngineKind;

    private void checkTemplate(String templatePath) {
        if (templatePath == null) {
            throw new IllegalArgumentException("Invalid templatePath");
        }

        if (!FileUtil.isFileExist(templatePath)) {
            throw new RuntimeException("Template file not exist: [" + templatePath + "]");
        }

        if (templateEngineKind == null) {
            throw new IllegalStateException("Invalid TemplateEngineKind config");
        }
    }

    public IXDocReport buildReport(String templatePath) {
        checkTemplate(templatePath);

        IXDocReport report = null;

        try {
            InputStream in = new FileInputStream(templatePath);

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
