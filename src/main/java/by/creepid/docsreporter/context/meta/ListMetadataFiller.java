/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.context.meta;

import by.creepid.docsreporter.utils.FieldHelper;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 *
 * @author rusakovich
 */
@Component
public class ListMetadataFiller implements FieldsMetadataFiller {

    @Override
    public void fillMetadata(FieldsMetadata metadataToFill, Class<?> modelClass, String modelName, Map<String, Class<?>> iterationNames) {
        Map<String, Field> result = FieldHelper.getFieldsOfClass(Collection.class, modelClass, modelName, true);

        for (Map.Entry<String, Field> entry : result.entrySet()) {

            ParameterizedType collectionType = (ParameterizedType) entry.getValue().getGenericType();
            Class<?> actualTypeArg = (Class<?>) collectionType.
                    getActualTypeArguments()[0];

            List<String> hierarchy = FieldHelper.getFieldHierarchy(
                    actualTypeArg, entry.getKey());

            for (String path : hierarchy) {
                metadataToFill.addFieldAsList(path);
            }

        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
