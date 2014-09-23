/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ejb.translatable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.*;
import org.jearl.logback.ErrorCategory;
import org.jearl.log.JearlLogger;
import org.jearl.log.Logger;
import org.jearl.util.PropertyUtils;

/**
 *
 * @author bamasyali
 */
public abstract class AbstractTranslatableEntity<T> {

    private static final LanguageEnum DEFAULT_LANGUAGE;
    private final JearlLogger logger;
    private Map<String, String> valueMap;
    private final String tableName;
    private final Map<String, String> columnNameMap;

    static {
        DEFAULT_LANGUAGE = LanguageEnum.TR;
    }

    public AbstractTranslatableEntity(Class<T> clazz) {
        this.logger = Logger.getJearlLogger(AbstractTranslatableEntity.class);
        this.valueMap = new HashMap<String, String>();
        Table e = (Table) clazz.getAnnotation(Table.class);
        if (e == null) {
            this.tableName = null;
        } else {
            this.tableName = e.name();
        }
        this.columnNameMap = new HashMap<String, String>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Column c = field.getAnnotation(Column.class);
            if (c == null) {
                continue;
            }
            columnNameMap.put(field.getName(), c.name());
        }
    }

    public abstract EntityManager getEntityManager();

    public abstract LanguageEnum getLanguage();

    public String getValue(T entity, String field) {
        String defaultValue = null;
        try {
            Object tempValue = PropertyUtils.getProperty(entity, field);
            if (tempValue == null) {
                defaultValue = null;
            } else {
                defaultValue = tempValue.toString();
            }
        } catch (IllegalAccessException ex) {
            logger.error(ex, ErrorCategory.DATABASE_ERROR, ex);
        } catch (NoSuchMethodException ex) {
            logger.error(ex, ErrorCategory.DATABASE_ERROR, ex);
        } catch (InvocationTargetException ex) {
            logger.error(ex, ErrorCategory.DATABASE_ERROR, ex);
        }

        if (DEFAULT_LANGUAGE != getLanguage()) {
            if (this.valueMap.get(field) != null) {
                return this.valueMap.get(field);
            } else {
                try {

                    Query query = getEntityManager().createNativeQuery("select text_text from lang_translate where text_table = ? and text_record = ? and text_field = ? and text_language = ?");
                    query.setParameter(1, tableName);
                    query.setParameter(2, getEntityKey(entity));
                    query.setParameter(3, columnNameMap.get(field));
                    query.setParameter(4, getLanguage().getLangCode());
                    String text = (String) query.getSingleResult();
                    this.valueMap.put(field, text);
                    return this.valueMap.get(field);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage() + " - " + ex.getCause());
                    return defaultValue;
                }
            }
        } else {
            return defaultValue;
        }
    }

    public void setValue(T entity, String field, String value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected Integer getEntityKey(T entity) {
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            Id primaryKey = field.getAnnotation(Id.class);
            EmbeddedId primaryKey2 = field.getAnnotation(EmbeddedId.class);
            if (primaryKey != null | primaryKey2 != null) {
                Object temp;
                try {
                    temp = PropertyUtils.getProperty(entity, field.getName());
                } catch (Exception ex) {
                    System.out.println(tableName + " " + this + " ex:" + ex);
                    temp = null;
                }
                if (temp == null) {
                    return null;
                } else if (temp instanceof Integer) {
                    return (Integer) temp;
                } else {
                    throw new UnsupportedOperationException("Class Type : " + temp.getClass().getName());
                }
            }
        }
        return null;
    }
}
