package by.creepid.docsreporter;

import by.creepid.docsreporter.context.validation.ReportProcessingException;
import by.creepid.docsreporter.converter.DocFormat;
import by.creepid.docsreporter.converter.images.ImageExtractObserver;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import java.io.ByteArrayOutputStream;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/test/resources/by/creepid/docsreporter/ExampleConfigurationTests-context.xml")
public class ReportTemplateTest {

    @Autowired
    private ReportTemplate reportTemplate;
    private static final String folder = "src/test/resources/";
    private static final String ext = ".pdf";
    private static final String imageName = "logo.jpeg";

    private byte[] getImage(String path) {
        File fi = new File(path);

        try {
            return Files.readAllBytes(fi.toPath());
        } catch (IOException ex) {
            Logger.getLogger(ReportTemplateTest.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

        throw new RuntimeException("Cannot set the photo");
    }

    private Project getProject() {
        Project project = new Project("project name", new Date(), new BigDecimal("123.123"));

        Manager manager = new Manager("Mike", "Green", 5);
        String string = "January 2, 1982";
        Date birthDate = null;
        try {
            birthDate = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(string);
        } catch (ParseException ex) {
            Logger.getLogger(ReportTemplateTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        manager.setBirthDate(birthDate);

        project.setManager(manager);

        project.add(new DeveloperWithPhoto("ZERR", "Angelo",
                "angelo.zerr@gmail.com", birthDate, this.getImage(folder + "photo2.jpeg")));
        project.add(new DeveloperWithPhoto("Leclercq", "Pascal",
                "pascal.leclercq@gmail.com", null, this.getImage(folder + "photo1.jpeg")));

        project.add(new DeveloperWithPhoto("Leclercq", "Pascal",
                null, birthDate, this.getImage(folder + "photo1.jpeg")));

        project.add(new DeveloperWithPhoto("Arnold", "Brown",
                "arnoldbrown@yahoo.com", birthDate, this.getImage(folder + "photo2.jpeg")));

        project.add(new DeveloperWithPhoto("Jim", "Smith",
                "jimmythebest@tut.by", birthDate, this.getImage(folder + "photo3.jpeg")));

        project.setLogo(this.getImage(folder + imageName));

        return project;
    }

    private String getTimestamp() {
        Calendar cal = Calendar.getInstance();
        return String.valueOf(cal.getTimeInMillis());
    }

    @Test
    public final void testGenerateReport() throws IOException, XDocReportException {
        // 1) Create FieldsMetadata by setting Velocity as template engine
        FieldsMetadata fieldsMetadata = new FieldsMetadata(TemplateEngineKind.Velocity.name());

        // 2) Load fields metadata from Java Class
        fieldsMetadata.load("project", Project.class);

        // 3) Generate XML fields in the file "project.fields.xml".
        // Extension *.fields.xml is very important to use it with MS Macro XDocReport.dotm
        // FieldsMetadata#saveXML is called with true to indent the XML.
        File xmlFieldsFile = new File(folder + "project.fields.xml");
        fieldsMetadata.saveXML(new FileOutputStream(xmlFieldsFile), true);

        ByteArrayOutputStream out = null;
        ImageExtractObserver observer = new ImageExtractObserver() {

            @Override
            public void getImage(byte[] content, String path) {
                System.out.println(path);
                OutputStream outFile = null;
                try {

                    File file = new File(folder + path);
                    file.getParentFile().mkdirs();
                    file.createNewFile();

                    outFile = new FileOutputStream(file);
                    outFile.write(content);

                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ReportTemplateTest.class.getName()).
                            log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ReportTemplateTest.class.getName()).
                            log(Level.SEVERE, null, ex);
                } finally {
                    if (outFile != null) {
                        try {
                            outFile.close();
                        } catch (IOException ex) {
                            Logger.getLogger(ReportTemplateTest.class.getName()).
                                    log(Level.SEVERE, null, ex);
                        }
                    }
                }

            }
        };

        OutputStream outFile = null;
        try {
            String docName = folder + "out_" + getTimestamp() + ext;
            Map<String, byte[]> map = new HashMap<>();

            out = (ByteArrayOutputStream) reportTemplate.generateReport(DocFormat.getFormat(docName), getProject(), observer);

            File file = new File(docName);
            file.createNewFile();
            outFile = new FileOutputStream(file);

            out.writeTo(outFile);
            if (!new File(docName).exists()) {
                fail("Report not generated");
            }
            outFile.close();

        } catch (ReportProcessingException ex) {

            System.out.println(ex.getMessage());
            Errors errors = ex.getErrors();
            if (errors != null) {
                List<ObjectError> errorsList = errors.getAllErrors();
                for (ObjectError objectError : errorsList) {
                    System.out.println(objectError.getDefaultMessage());
                    System.out.println(objectError.getCode());
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
                Logger.getLogger(ReportTemplateTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
