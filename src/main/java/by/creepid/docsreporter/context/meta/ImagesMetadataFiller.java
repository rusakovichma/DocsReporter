/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.context.meta;

import by.creepid.docsreporter.context.annotations.Image;
import by.creepid.docsreporter.utils.FieldHelper;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.NullImageBehaviour;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 *
 * @author rusakovich
 */
@Component
public class ImagesMetadataFiller implements FieldsMetadataFiller {

    private static final Class<? extends Annotation> imageAnnotation = Image.class;

    private static final NullImageBehaviour DEFAULT_BEHAVIOR = NullImageBehaviour.RemoveImageTemplate;
    private NullImageBehaviour behaviour = DEFAULT_BEHAVIOR;

    protected List<String> getAliases(Class<?> iterClass, Map<String, Class<?>> iteratorNames) {
        if (iteratorNames == null || iteratorNames.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> aliases = new ArrayList<>();

        Set<Map.Entry<String, Class<?>>> entries = iteratorNames.entrySet();
        for (Map.Entry<String, Class<?>> entry : entries) {
            if (entry.getValue() == iterClass) {
                aliases.add(entry.getKey());
            }
        }

        return aliases;
    }

    @Override
    public void fillMetadata(FieldsMetadata metadataToFill, Class<?> modelClass, String modelName, Map<String, Class<?>> iterationNames) {
        Map<String, Field> fields = FieldHelper.getAnnotatedTypeArgumentFields(modelClass, modelName, imageAnnotation, true);
        for (Field field : fields.values()) {

            String name = field.getName();
            if (field.getType() != byte[].class) {
                throw new IllegalStateException("Image field [" + name + "] must have byte array type!");
            }

            Image imageAnnot = (Image) field.getAnnotation(imageAnnotation);

            List<String> aliases = getAliases(field.getDeclaringClass(), iterationNames);
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
                            FieldHelper.getFieldPath(field.getDeclaringClass().getSimpleName(), field.getName()), behaviour);
                }
            }
        }

        Map<String, Field> fieldMap = FieldHelper.getAnnotatedDeclaredFields(modelClass, modelName, imageAnnotation, true);
        Set<Map.Entry<String, Field>> entries = fieldMap.entrySet();
        for (Map.Entry<String, Field> entry : entries) {
            Image imageAnnot = (Image) entry.getValue().getAnnotation(imageAnnotation);

            String[] bookmarks = imageAnnot.bookmarks();
            for (String bookmark : bookmarks) {
                metadataToFill.addFieldAsImage(bookmark, entry.getKey(), behaviour);
            }

        }
    }

    @Override
    public int getOrder() {
        return 1;
    }

    public void setBehaviour(NullImageBehaviour behaviour) {
        this.behaviour = behaviour;
    }

}
