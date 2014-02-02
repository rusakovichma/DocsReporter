package by.creepid.docsreporter;

import by.creepid.docsreporter.converter.DocFormat;
import java.io.ByteArrayOutputStream;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
            Logger.getLogger(ReportTemplateTest.class.getName()).log(Level.SEVERE, null, ex);
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
    public final void testGenerateReport() throws Exception {
        String docName = folder + "out_" + getTimestamp() + ext;
        File file = new File(docName);
        file.createNewFile();
        OutputStream outFile = new FileOutputStream(file);

        ByteArrayOutputStream out = (ByteArrayOutputStream) reportTemplate.generateReport(DocFormat.getFormat(docName) , getProject());

        out.writeTo(outFile);

        if (!new File(docName).exists()) {
            fail("Report not generated");
        }

        outFile.close();
        out.close();

    }
}
