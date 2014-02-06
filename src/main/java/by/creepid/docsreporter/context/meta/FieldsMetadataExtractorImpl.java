/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.context.meta;

import by.creepid.docsreporter.context.annotations.ImageField;
import by.creepid.docsreporter.utils.FieldHelper;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.NullImageBehaviour;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author mirash
 */
public class FieldsMetadataExtractorImpl implements FieldsMetadataExtractor {

    private static final Class<? extends Annotation> imageAnnotation = ImageField.class;
    private static final NullImageBehaviour DEFAULT_BEHAVIOR = NullImageBehaviour.KeepImageTemplate;
    private NullImageBehaviour behaviour = DEFAULT_BEHAVIOR;

    private List<String> getAliases(Class<?> iterClass, Map<String, Class<?>> iteratorNames) {
        if (iteratorNames == null || iteratorNames.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> aliases = new ArrayList<>();

        Set<Entry<String, Class<?>>> entries = iteratorNames.entrySet();
        for (Entry<String, Class<?>> entry : entries) {
            if (entry.getValue() == iterClass) {
                aliases.add(entry.getKey());
            }
        }

        return aliases;
    }

    private void extractImagesMetadata(FieldsMetadata metadataToFill, Class<?> modelClass, String modelName, Map<String, Class<?>> iteratorNames) {

        Field[] fields = FieldHelper.getAnnotatedTypeArgumentFields(modelClass, Collection.class, imageAnnotation, true);
        for (Field field : fields) {
            String name = field.getName();
            if (field.getType() != byte[].class) {
                throw new IllegalStateException("Image field [" + name + "] must have byte array type!");
            }

            ImageField imageAnnot = (ImageField) field.getAnnotation(imageAnnotation);

            List<String> aliases = getAliases(field.getDeclaringClass(), iteratorNames);
            if (!aliases.isEmpty()) {
                for (String alias : aliases) {

                    String[] bookmarks = imageAnnot.bookmarks();
                    for (String bookmark : bookmarks) {

                        metadataToFill.addFieldAsImage(
                                bookmark,
                                FieldHelper.getFieldPath(alias, field.getName()));
                    }
                }
            } else {
                String[] bookmarks = imageAnnot.bookmarks();
                for (String bookmark : bookmarks) {

                    metadataToFill.addFieldAsImage(
                            bookmark,
                            FieldHelper.getFieldPath(field.getDeclaringClass().getSimpleName(), field.getName()));
                }
            }
        }

        Map<String, Field> fieldMap = FieldHelper.getAnnotatedDeclaredFields(modelClass, modelName, imageAnnotation, true);
        Set<Map.Entry<String, Field>> entries = fieldMap.entrySet();
        for (Entry<String, Field> entry : entries) {
            ImageField imageAnnot = (ImageField) entry.getValue().getAnnotation(imageAnnotation);

            String[] bookmarks = imageAnnot.bookmarks();
            for (String bookmark : bookmarks) {
                metadataToFill.addFieldAsImage(bookmark, entry.getKey());
            }

        }

    }

    public void fillMetadata(FieldsMetadata metadataToFill, Class<?> modelClass, String modelName, Map<String, Class<?>> iteratorNames) {
        metadataToFill.setBehaviour(behaviour);

        this.extractImagesMetadata(metadataToFill, modelClass, modelName, iteratorNames);
    }

    public void setBehaviour(NullImageBehaviour behaviour) {
        this.behaviour = behaviour;
    }
}
