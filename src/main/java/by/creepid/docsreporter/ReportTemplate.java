package by.creepid.docsreporter;

import by.creepid.docsreporter.converter.DocFormat;
import java.io.OutputStream;

public interface ReportTemplate {

    public OutputStream generateReport(DocFormat targetFormat, Object model)
            throws Exception;

    public String getTemplatePath();

    public Class<?> getModelClass();
}
