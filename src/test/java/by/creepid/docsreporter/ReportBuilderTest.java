package by.creepid.docsreporter;

import static org.junit.Assert.fail;

import java.io.File;
import java.math.BigDecimal;
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
public class ReportBuilderTest {

    @Autowired
    private ReportBuilder reportBuilder;

    private static final String folder = "src/test/resources/";
    private static final String ext = ".htm";

    private Project getProject() {
        Project project = new Project("project name", new Date(), new BigDecimal("123.123"));
          
        Manager manager = new Manager("Mike", "Green", 5);
        String string = "January 2, 1982";
        Date birthDate = null;
        try {
            birthDate = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(string);
        } catch (ParseException ex) {
            Logger.getLogger(ReportBuilderTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        manager.setBirthDate(birthDate);
        
        project.setManager(manager);
        
        return project;
    }

    private String getTimestamp() {
        Calendar cal = Calendar.getInstance();
        return String.valueOf(cal.getTimeInMillis());
    }

    @Test
    public final void testGenerateReport() throws Exception {
        String docName = folder + "out_" + getTimestamp() + ext;
        reportBuilder.generateReport(docName, "project", getProject());

        if (!new File(docName).exists()) {
            fail("Report not generated");
        }

        docName = folder + "out_" + getTimestamp() + ext;
        reportBuilder.generateReport(docName, "project", getProject());

        if (!new File(docName).exists()) {
            fail("Report not generated");
        }
    }
    
}
