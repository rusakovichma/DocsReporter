/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.context.meta;

import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import java.util.Map;
import org.springframework.core.Ordered;

/**
 *
 * @author mirash
 */
public interface FieldsMetadataFiller extends Ordered {

    public void fillMetadata(FieldsMetadata metadataToFill, Class<?> modelClass, String modelName, Map<String, Class<?>> iterationNames);

}
