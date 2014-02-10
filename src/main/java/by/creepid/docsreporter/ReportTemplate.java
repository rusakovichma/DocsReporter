package by.creepid.docsreporter;

import by.creepid.docsreporter.context.validation.ReportProcessingException;
import by.creepid.docsreporter.converter.DocFormat;
import by.creepid.docsreporter.converter.images.ImageExtractObserver;
import java.io.OutputStream;

public interface ReportTemplate {

    public OutputStream generateReport(DocFormat targetFormat, Object model, ImageExtractObserver observer)
            throws ReportProcessingException;

    public String getTemplatePath();

    public Class<?> getModelClass();

}
