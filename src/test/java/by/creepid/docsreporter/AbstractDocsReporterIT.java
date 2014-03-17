/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter;

import by.creepid.docsreporter.model.DeveloperWithPhoto;
import by.creepid.docsreporter.model.Project;
import by.creepid.docsreporter.model.Role;
import by.creepid.docsreporter.model.WorkingStatus;
import by.creepid.docsreporter.model.Manager;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author rusakovich
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/test/resources/by/creepid/docsreporter/ExampleConfigurationTests-context.xml")
public abstract class AbstractDocsReporterIT {

    public static final String photoFolder = "src/test/resources/photoSamples/";
    public static final String targetFolder = "src/test/resources/";

    private static final String imageName = "logo.jpeg";

    protected Project createProjectSample() {
        Project project = new Project("project name", new Date(), new BigDecimal("123.123"));
        
        project.setUrl("https://github.com/creepid/DocsReporter");
        
        Manager manager = new Manager("Mike", "<b>Green</b>", 5);
        String string = "January 2, 1982";
        Date birthDate = null;
        try {
            birthDate = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(string);
        } catch (ParseException ex) {
            Logger.getLogger(ReportTemplateIT.class.getName()).log(Level.SEVERE, null, ex);
        }

        List<Role> roles = new ArrayList<Role>();
        roles.add(new Role("Programmer"));
        roles.add(new Role("GUI programmer"));

        manager.setBirthDate(birthDate);

        project.setManager(manager);

        project.add(new DeveloperWithPhoto("<b>ZERR</b>", "Angelo",
                "angelo.zerr@gmail.com", birthDate, this.getFileBytes(photoFolder + "photo2.jpeg"), roles));
        project.add(new DeveloperWithPhoto("<b>Leclercq</b>", "Pascal",
                "pascal.leclercq@gmail.com", null, this.getFileBytes(photoFolder + "photo1.jpeg"), roles, WorkingStatus.freelance));

        project.add(new DeveloperWithPhoto("<b>Leclercq</b>", "Pascal",
                null, birthDate, this.getFileBytes(photoFolder + "photo1.jpeg"), roles));

        roles = new ArrayList<Role>();
        roles.add(new Role("System programmer"));
        roles.add(new Role("Admin"));
        project.add(new DeveloperWithPhoto("<b>Arnold</b>", "Brown",
                "arnoldbrown@yahoo.com", birthDate, this.getFileBytes(photoFolder + "photo2.jpeg"), roles));

        roles = new ArrayList<Role>();
        roles.add(new Role("Architect"));
        project.add(new DeveloperWithPhoto("<b>Jim</b>", "Smith",
                "jimmythebest@tut.by", birthDate, this.getFileBytes(photoFolder + "photo3.jpeg"), roles, WorkingStatus.halfTime));

        project.setLogo(this.getFileBytes(photoFolder + imageName));

        return project;
    }

    protected byte[] getFileBytes(String path) {
        File fi = new File(path);

        try {
            return Files.readAllBytes(fi.toPath());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        throw new RuntimeException("Cannot set the photo");
    }

    protected String getTimestamp() {
        Calendar cal = Calendar.getInstance();
        return String.valueOf(cal.getTimeInMillis());
    }

}
