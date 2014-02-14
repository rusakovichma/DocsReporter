/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.context.meta;

import by.creepid.docsreporter.AbstractDocsReporterIT;
import by.creepid.docsreporter.model.Project;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author rusakovich
 */
public class FieldsMetadataBuilderIT extends AbstractDocsReporterIT {

    @Autowired
    private FieldsMetadataBuilder instance;

    /**
     * Test of getMetaInXML method, of class FieldsMetadataBuilder.
     */
    @Test
    public void testGetMetaInXML() throws Exception {
        System.out.println("*** getMetaInXML ******");
        Class modelClass = Project.class;
        String modelName = "project";

        FileOutputStream fileOutputStream = new FileOutputStream(targetFolder + modelName + ".xml");
        ByteArrayOutputStream meta = (ByteArrayOutputStream) instance.getMetaInXML(modelClass, modelName);

        fileOutputStream.write(meta.toByteArray());
    }

}
