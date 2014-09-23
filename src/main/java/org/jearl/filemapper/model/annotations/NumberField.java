/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.filemapper.model.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author faik
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NumberField {

    int position();

    int size();

    char fill();

    boolean real();

    boolean separator();

    int realSize();

    int numberSize() default 0;

    boolean convert() default false;

    TextFloat textFloat();
}
