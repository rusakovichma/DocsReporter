package by.creepid.docsreporter;

import by.creepid.docsreporter.context.ContextFactory;
import by.creepid.docsreporter.context.DocReportFactory;
import by.creepid.docsreporter.context.meta.FieldsMetadataExtractor;
import by.creepid.docsreporter.context.validation.ReportFieldsValidator;
import by.creepid.docsreporter.context.validation.ReportProcessingException;
import by.creepid.docsreporter.converter.DocConverterAdapter;
import by.creepid.docsreporter.converter.DocFormat;
import static by.creepid.docsreporter.converter.DocFormat.*;
import by.creepid.docsreporter.converter.ImageExtractor;
import fr.opensagres.xdocreport.core.XDocReportException;
import java.io.OutputStream;

import org.springframework.stereotype.Service;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.template.FieldsExtractor;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

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

    private Validator reportValidator;

    private volatile Errors fieldErrors;

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

        reportValidator = new ReportFieldsValidator(modelClass, modelName);
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

    protected OutputStream convert(DocFormat targetFormat, InputStream in, Map<String, byte[]> imgs)
            throws Exception {
        DocConverterAdapter converter = findConverter(targetFormat);

        ByteArrayOutputStream out;
        out = (ByteArrayOutputStream) converter.convert(targetFormat, in);

        ImageExtractor extractor = converter.getImageExtractor();
        if (extractor != null) {

            Iterator<Map.Entry<String, byte[]>> iter = extractor.getImageIterator();
            while (iter.hasNext()) {
                Map.Entry<String, byte[]> entry = iter.next();
                imgs.put(entry.getKey(), entry.getValue());
                iter.remove();
            }

        }

        return out;
    }

    private synchronized void validate(Object model)
            throws ReportProcessingException {
        try {
            FieldsExtractor fieldsExtractor = new FieldsExtractor();

            docReport.extractFields(fieldsExtractor);
            fieldErrors = new BeanPropertyBindingResult(model, modelName);

            ValidationUtils.invokeValidator(reportValidator, fieldsExtractor, fieldErrors);

            if (fieldErrors.hasErrors()) {
                throw new ReportProcessingException("Invalid report fields found");
            }
        } catch (XDocReportException | IOException | ReportProcessingException ex) {
            throw new ReportProcessingException(ex);
        }
    }

    @Override
    public OutputStream generateReport(DocFormat targetFormat, Object model, Map<String, byte[]> imgs)
            throws ReportProcessingException {
        if (targetFormat == UNSUPPORTED) {
            throw new ReportProcessingException("Given format is not supported!");
        }

        if (!modelClass.isAssignableFrom(model.getClass())) {
            throw new ReportProcessingException("Given class is not implements: " + modelClass.getName());
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            validate(model);

            getContext().put(modelName, model);

            synchronized (DocReportTemplate.class) {
                docReport.process(getContext(), out);
            }

            if (targetFormat != templateFormat) {
                InputStream in = new ByteArrayInputStream(out.toByteArray());
                out = (ByteArrayOutputStream) convert(targetFormat, in, imgs);
            }

        } catch (ReportProcessingException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ReportProcessingException(ex);
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

    public Errors getFieldErrors() {
        return fieldErrors;
    }

}
