/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.context.validation;

import by.creepid.docsreporter.utils.FieldHelper;
import fr.opensagres.xdocreport.template.FieldExtractor;
import fr.opensagres.xdocreport.template.FieldsExtractor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 * @author rusakovich
 */
public class ReportFieldsValidator implements Validator {

    private static final String FIELD_INVALID_MESSAGE = "Incalid field name";
    private static final String FIELD_LIST_INVALID_MESSAGE = "Invalid list field name";
    private static List<String> templateSystemPrefixes
            = Arrays.asList("{", "___", "velocity", "freemarker", "list", "foreach");

    private final Class<?> modelClass;
    private final String modelName;
    private final List<String> fieldHierarchy;

    static {
        templateSystemPrefixes = Collections.unmodifiableList(templateSystemPrefixes);
    }

    public ReportFieldsValidator(Class<?> modelClass, String modelName) {
        this.modelClass = modelClass;
        this.modelName = modelName;

        fieldHierarchy = FieldHelper.getFieldHierarchy(modelClass, modelName);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FieldsExtractor.class.isAssignableFrom(clazz);
    }

    protected boolean isTemplateSystemName(String str) {

        for (String prefix : templateSystemPrefixes) {
            if (str.startsWith(prefix)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target instanceof FieldsExtractor) {
            FieldsExtractor extractor = (FieldsExtractor) target;

            List<FieldExtractor> fieldsExtractor = extractor.getFields();
            for (FieldExtractor fieldExtractor : fieldsExtractor) {
                String name = fieldExtractor.getName();

                if (!isTemplateSystemName(name) && !fieldHierarchy.contains(name)) {

                    String message = (fieldExtractor.isList())
                            ? FIELD_LIST_INVALID_MESSAGE
                            : FIELD_INVALID_MESSAGE;

                    errors.reject(name, message);
                }

            }

        }
    }

}
