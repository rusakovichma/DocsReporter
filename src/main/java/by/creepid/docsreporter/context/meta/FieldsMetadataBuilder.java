/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.context.meta;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author rusakovich
 */
@Component
public class FieldsMetadataBuilder {

    @Autowired
    private TemplateEngineKind templateEngineKind;

    public OutputStream getMetaInXML(Class modelClass, String modelName)
            throws IOException, XDocReportException {
        FieldsMetadata fieldsMetadata = new FieldsMetadata(templateEngineKind);

        // 2) Load fields metadata from Java Class
        fieldsMetadata.load(modelName, modelClass);

        // 3) Generate XML fields in the file "project.fields.xml".
        // Extension *.fields.xml is very important to use it with MS Macro XDocReport.dotm
        // FieldsMetadata#saveXML is called with true to indent the XML.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        fieldsMetadata.saveXML(outputStream, true);

        return outputStream;
    }

    public void setTemplateEngineKind(TemplateEngineKind templateEngineKind) {
        this.templateEngineKind = templateEngineKind;
    }

}
