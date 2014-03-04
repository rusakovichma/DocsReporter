/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.context.validation;

import by.creepid.docsreporter.context.AppContextManager;
import by.creepid.docsreporter.context.fields.FieldsFilter;
import by.creepid.docsreporter.utils.FieldHelper;
import fr.opensagres.xdocreport.template.FieldExtractor;
import fr.opensagres.xdocreport.template.FieldsExtractor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 * @author rusakovich
 */
public class ReportFieldsValidator implements Validator {

    private static final String FIELD_INVALID_MESSAGE = "Incalid field name";
    private static final String FIELD_LIST_INVALID_MESSAGE = "Invalid list field name";

    private final Class<?> modelClass;
    private final String modelName;
    private final List<String> fieldHierarchy;
    private final Map<String, Class<?>> iteratorNames;

    private final FieldsFilter fieldsFilter;

    private void addSynonyms() {
        Set<Map.Entry<String, Class<?>>> iterEntries = iteratorNames.entrySet();

        List<String> synonymPathes = new ArrayList<>();

        for (Map.Entry<String, Class<?>> entry : iterEntries) {
            List<String> pathes = FieldHelper.getFieldPath(entry.getValue(), modelClass, modelName);

            for (String path : pathes) {
                for (String fieldPath : fieldHierarchy) {
                    if (fieldPath.indexOf(path) != -1) {
                        synonymPathes.add(fieldPath.replaceFirst(path, entry.getKey()));
                    }

                }
            }

        }

        fieldHierarchy.addAll(synonymPathes);
    }

    public ReportFieldsValidator(Class<?> modelClass, String modelName, Map<String, Class<?>> iteratorNames) {
        this.modelClass = modelClass;
        this.modelName = modelName;
        this.iteratorNames = iteratorNames;

        fieldHierarchy = FieldHelper.getFieldHierarchy(modelClass, modelName);

        if (iteratorNames != null) {
            addSynonyms();
        }

        this.fieldsFilter = AppContextManager.getbean(FieldsFilter.class);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FieldsExtractor.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target instanceof FieldsExtractor) {
            FieldsExtractor extractor = (FieldsExtractor) target;

            List<FieldExtractor> fieldsExtractor = extractor.getFields();
            for (FieldExtractor fieldExtractor : fieldsExtractor) {
                String name = fieldExtractor.getName();

                if (!fieldsFilter.isInFilterList(name) && !fieldHierarchy.contains(name)) {

                    String message = (fieldExtractor.isList())
                            ? FIELD_LIST_INVALID_MESSAGE
                            : FIELD_INVALID_MESSAGE;

                    errors.reject(name, message);
                }

            }

        }
    }

}
