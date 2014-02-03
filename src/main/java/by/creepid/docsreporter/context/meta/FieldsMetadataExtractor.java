/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.context.meta;

import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import java.util.Map;

/**
 *
 * @author mirash
 */
public interface FieldsMetadataExtractor {

    public void fillMetadata(FieldsMetadata metadataToFill, Class<?> modelClass, String modelName, Map<String, Class<?>> iteratorNames);
}
