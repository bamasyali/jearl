package org.jearl.filemapper.model.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by IntelliJ IDEA. User: taskin Date: 13.Ara.2010 Time: 13:45:45 To
 * change this template use File | Settings | File Templates.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateField {

    int position();

    String pattern();//dd.MM.yyyy hh:mm:

    int size();
}
