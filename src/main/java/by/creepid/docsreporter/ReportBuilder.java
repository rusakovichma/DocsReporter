package by.creepid.docsreporter;

public interface ReportBuilder {

    public void generateReport(String reportOutputPath, String modelName, Object model)
            throws Exception;

}
