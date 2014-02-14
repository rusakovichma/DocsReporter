/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package by.creepid.docsreporter.context.meta;

import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.format.Printer;
import org.springframework.stereotype.Component;

/**
 *
 * @author rusakovich
 */
@Component
public class MetadataFillerChain {
    
    @Autowired
    private List<FieldsMetadataFiller> fillers;
    
    @PostConstruct
    public void init() {
        Collections.sort(fillers, AnnotationAwareOrderComparator.INSTANCE);
    }
    
    public void fillMetadata(FieldsMetadata metadataToFill, Class<?> modelClass, String modelName, Map<String, Class<?>> iterationNames) {
        for (FieldsMetadataFiller filler : fillers) {
            filler.fillMetadata(metadataToFill, modelClass, modelName, iterationNames);
        }
    }
    
}
