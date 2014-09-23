/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.email.template;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import org.jearl.email.EMailSender;
import org.jearl.email.exception.EMailException;
import org.jearl.email.model.EMail;
import org.jearl.email.model.EMailProperties;
import org.jearl.email.template.model.EMailTemplateParameter;
import org.jearl.util.PropertyUtils;
import org.jearl.util.StreamUtils;

/**
 *
 * @author bamasyali
 */
public abstract class EMailTemplate<T extends EMailProperties, USER> {

    protected final String ip;
    protected final USER user;

    public EMailTemplate(String ip, USER user) {
        this.ip = ip;
        this.user = user;
    }

    public abstract InputStream getInputStream();

    public abstract String getTopic();

    public abstract EMailSender<T> getMailSender();

    public abstract EMail getEMail();

    public abstract T getProperties();

    public void sendMail() throws EMailException {
        EMailSender<T> ems = getMailSender();
        ems.sendMail(getProperties(), getEMail(), true);
    }

    public String getString() throws EMailException, IOException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        InputStream is = getInputStream();
        String template = StreamUtils.convertInputStreamToString(is);
        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields) {
            EMailTemplateParameter emtp = field.getAnnotation(EMailTemplateParameter.class);
            if (emtp == null) {
                continue;
            }
            Object value = PropertyUtils.getProperty(this, field.getName());
            if (value == null && !emtp.nullable()) {
                throw new EMailException();
            }
            if (value == null) {
                template = template.replace("<" + emtp.name() + ">", "");
            } else {
                template = template.replace("<" + emtp.name() + ">", value.toString());
            }

        }
        return template;
    }
}
