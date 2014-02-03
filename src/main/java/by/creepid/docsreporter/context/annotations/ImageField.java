/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.context.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author mirash
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ImageField {
    
    public String[] bookmarks();

    public boolean useTemplateSize() default true;

    public boolean useForcedSize() default false;

    public float width() default Float.NaN;

    public float height() default Float.NaN;

    public boolean useRatioSize() default false;
}