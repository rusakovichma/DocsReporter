package by.creepid.docsreporter;

import by.creepid.docsreporter.context.validation.ReportProcessingException;
import by.creepid.docsreporter.converter.DocFormat;
import java.io.OutputStream;
import org.springframework.validation.Errors;

public interface ReportTemplate {

    public OutputStream generateReport(DocFormat targetFormat, Object model)
            throws ReportProcessingException;

    public String getTemplatePath();

    public Class<?> getModelClass();

    public Errors getFieldErrors();
}
