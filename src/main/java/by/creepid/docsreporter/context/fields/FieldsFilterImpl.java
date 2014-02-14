/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.context.fields;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;

/**
 *
 * @author rusakovich
 */
public class FieldsFilterImpl implements FieldsFilter {

    private List<String> templateSystemPrefixes
            = Arrays.asList("{", "___", "velocity", "freemarker", "list", "foreach", "item_");

    @PostConstruct
    public void init() {
        templateSystemPrefixes = Collections.unmodifiableList(templateSystemPrefixes);
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
    public boolean isInFilterList(String field) {
        return isTemplateSystemName(field);
    }

}
