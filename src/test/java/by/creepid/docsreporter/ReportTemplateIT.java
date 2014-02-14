package by.creepid.docsreporter;

import by.creepid.docsreporter.model.Project;
import by.creepid.docsreporter.context.validation.ReportProcessingException;
import by.creepid.docsreporter.converter.DocFormat;
import by.creepid.docsreporter.converter.images.ImageExtractObserver;
import fr.opensagres.xdocreport.core.XDocReportException;
import java.io.ByteArrayOutputStream;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

public class ReportTemplateIT extends AbstractDocsReporterIT {

    @Autowired
    private ReportTemplate reportTemplate;

    private static final String ext = ".pdf";

    private Project project;

    @Before
    public void setUp() {
        project = createProjectSample();
    }

    @Test
    public final void testGenerateReport() throws IOException, XDocReportException {

        ByteArrayOutputStream out = null;
        ImageExtractObserver observer = new ImageExtractObserver() {

            @Override
            public void getImage(byte[] content, String path) {
                System.out.println("Save photo in: [" + path + "]");
                OutputStream outFile = null;
                try {

                    File file = new File(targetFolder + path);
                    file.getParentFile().mkdirs();
                    file.createNewFile();

                    outFile = new FileOutputStream(file);
                    outFile.write(content);

                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    if (outFile != null) {
                        try {
                            outFile.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }

            }
        };

        OutputStream outFile = null;
        try {
            String docName = targetFolder + "out_" + getTimestamp() + ext;

            Map<String, byte[]> map = new HashMap<>();

            out = (ByteArrayOutputStream) reportTemplate.generateReport(DocFormat.getFormat(docName), project, observer);

            File file = new File(docName);
            file.createNewFile();
            outFile = new FileOutputStream(file);

            out.writeTo(outFile);
            if (!new File(docName).exists()) {
                fail("Report not generated");
            }
            outFile.close();

        } catch (ReportProcessingException ex) {
            ex.printStackTrace();

            Errors errors = ex.getErrors();
            if (errors != null) {
                List<ObjectError> errorsList = errors.getAllErrors();
                for (ObjectError objectError : errorsList) {
                    fail(objectError.getDefaultMessage());
                    fail(objectError.getCode());
                }
            }
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (outFile != null) {
                    outFile.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
