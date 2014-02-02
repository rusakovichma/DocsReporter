package by.creepid.docsreporter;

import by.creepid.docsreporter.context.ContextFactory;
import by.creepid.docsreporter.context.DocReportFactory;
import by.creepid.docsreporter.context.meta.FieldsMetadataExtractor;
import by.creepid.docsreporter.converter.DocConverterAdapter;
import by.creepid.docsreporter.converter.DocFormat;
import static by.creepid.docsreporter.converter.DocFormat.*;
import fr.opensagres.xdocreport.core.XDocReportException;
import java.io.OutputStream;

import org.springframework.stereotype.Service;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.template.FieldExtractor;
import fr.opensagres.xdocreport.template.FieldsExtractor;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class DocReportTemplate implements ReportTemplate {

    private static final String SAX_DRIVER_PROP = "org.xml.sax.driver";
    @Autowired
    private DocReportFactory docReportFactory;
    @Autowired
    private ContextFactory contextFactory;
    @Resource(name = "docConverters")
    private List<DocConverterAdapter> docConverters;
    @Autowired(required = false)
    private FieldsMetadataExtractor metadataExtractor;
    private ThreadLocal<IContext> contextLocal;
    private IXDocReport docReport;
    private Class<?> modelClass;
    private String modelName;
    private String templatePath;
    private DocFormat templateFormat;
    private FieldsMetadata metadata;
    private Map<String, Class<?>> iteratorNames;

    public DocReportTemplate() {
        metadata = new FieldsMetadata();
    }

    public void initContext() {
        templateFormat = getFormat(templatePath);
        if (templateFormat == UNSUPPORTED) {
            throw new IllegalStateException("Given template format is not supported!");
        }

        if (modelClass == null) {
            throw new IllegalStateException("Model class must be set!");
        }

        docReport = docReportFactory.buildReport(templatePath);
        contextLocal = new ThreadLocal<IContext>();


        if (metadataExtractor != null) {
            metadataExtractor.fillMetadata(metadata, modelClass, modelName, iteratorNames);
        }

        docReport.setFieldsMetadata(metadata);

        clearSAXDriverProperty();
    }

    private IContext getContext() {
        if (contextLocal.get() == null) {
            contextLocal.set(
                    contextFactory.buildContext(docReport));
        }

        return contextLocal.get();
    }

    private void clearSAXDriverProperty() {
        if (System.getProperty(SAX_DRIVER_PROP) != null) {
            System.clearProperty(SAX_DRIVER_PROP);
        }
    }

    private DocConverterAdapter findConverter(DocFormat format) {
        if (docConverters == null) {
            throw new IllegalStateException("No document converters found!");
        }

        for (DocConverterAdapter docConverter : docConverters) {
            if (docConverter != null
                    && docConverter.getTargetFormat() == format
                    && docConverter.getSourceFormat() == templateFormat) {
                return docConverter;
            }
        }

        throw new IllegalStateException("No document converters found!");
    }

    protected OutputStream convert(DocFormat targetFormat, InputStream in)
            throws Exception {
        DocConverterAdapter converter = findConverter(targetFormat);

        ByteArrayOutputStream out;
        synchronized (DocReportTemplate.class) {
            out = (ByteArrayOutputStream) converter.convert(targetFormat, in);
        }

        return out;
    }

    @Override
    public OutputStream generateReport(DocFormat targetFormat, Object model)
            throws IOException, XDocReportException, Exception {
        if (targetFormat == UNSUPPORTED) {
            throw new IllegalArgumentException("Given format is not supported!");
        }

        getContext().put(modelName, model);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        synchronized (DocReportTemplate.class) {
            docReport.process(getContext(), out);
        }

        if (targetFormat != templateFormat) {
            InputStream in = new ByteArrayInputStream(out.toByteArray());
            out = (ByteArrayOutputStream) convert(targetFormat, in);
        }

        return out;
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

    public void setDocConverters(List<DocConverterAdapter> docConverters) {
        this.docConverters = docConverters;
    }

    public void setContextFactory(ContextFactory contextFactory) {
        this.contextFactory = contextFactory;
    }

    public Class<?> getModelClass() {
        return modelClass;
    }

    public void setModelClass(Class<?> modelClass) {
        this.modelClass = modelClass;
    }

    public void setMetadataExtractor(FieldsMetadataExtractor metadataExtractor) {
        this.metadataExtractor = metadataExtractor;
    }

    public void setBeforeRowToken(String beforeRowToken) {
        metadata.setBeforeRowToken(beforeRowToken);
    }

    public void setAfterRowToken(String afterRowToken) {
        metadata.setAfterRowToken(afterRowToken);
    }

    public void setBeforeTableCellToken(String beforeTableCellToken) {
        metadata.setBeforeTableCellToken(beforeTableCellToken);
    }

    public void setAfterTableCellToken(String afterTableCellToken) {
        metadata.setAfterTableCellToken(afterTableCellToken);
    }

    public void setIteratorNames(Map<String, Class<?>> iteratorNames) {
        this.iteratorNames = iteratorNames;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
}
