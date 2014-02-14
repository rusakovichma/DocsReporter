/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.context.meta;

import by.creepid.docsreporter.context.annotations.TextStyling;
import by.creepid.docsreporter.utils.FieldHelper;
import fr.opensagres.xdocreport.core.document.SyntaxKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 *
 * @author rusakovich
 */
@Component
public class TextStylingMetadataFiller implements FieldsMetadataFiller {

    private static final Class<? extends Annotation> textStylingAnnotation = TextStyling.class;

    @Override
    public void fillMetadata(FieldsMetadata metadataToFill, Class<?> modelClass, String modelName, Map<String, Class<?>> iterationNames) {
        Map<String, Field> fields = FieldHelper.getAnnotatedTypeArgumentFields(modelClass, modelName, textStylingAnnotation, true);

        for (Map.Entry<String, Field> entry : fields.entrySet()) {

            TextStyling stylingAnnot = (TextStyling) entry.
                    getValue().getAnnotation(textStylingAnnotation);

            SyntaxKind syntax = stylingAnnot.syntaxKind();
            String fieldPath = entry.getKey();

            metadataToFill.addFieldAsTextStyling(fieldPath, syntax);

            Class declaringClass = entry.getValue().getDeclaringClass();

            Set<Map.Entry<String, Class<?>>> iterEntries = iterationNames.entrySet();
            for (Map.Entry<String, Class<?>> iterEntry : iterEntries) {

                if (declaringClass.isAssignableFrom(iterEntry.getValue())) {
                    List<String> pathes = FieldHelper.getFieldPath(iterEntry.getValue(), modelClass, modelName);

                    for (String path : pathes) {
                        if (fieldPath.indexOf(path) != -1) {

                            metadataToFill.addFieldAsTextStyling(
                                    fieldPath.replaceFirst(path, iterEntry.getKey()), syntax);
                        }
                    }
                }

            }

        }
    }

    @Override
    public int getOrder() {
        return 2;
    }

}
