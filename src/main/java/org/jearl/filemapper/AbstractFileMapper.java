/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.filemapper;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.jearl.filemapper.exception.FileException;
import org.jearl.filemapper.model.annotations.*;
import org.jearl.logback.Log;
import org.jearl.logback.LogService;
import org.jearl.util.PropertyUtils;

/**
 *
 * @author bamasyali
 */
public abstract class AbstractFileMapper<T> implements FileMapper<T> {

    private final Class<T> entityClass;
    private final Map<String, StringField> stringMap;
    private final Map<String, DateField> dateMap;
    private final Map<String, NumberField> numberMap;
    private final Integer rowSize;
    private final Integer dumpRowSize;
    private final Log log;

    public AbstractFileMapper(Class<T> entityClass) throws FileException {
        this.log = new LogService(AbstractFileMapper.class);
        this.entityClass = entityClass;
        this.stringMap = findStringFields(entityClass);
        this.dateMap = findDateFields(entityClass);
        this.numberMap = findNumberFields(entityClass);
        Annotation fileEntity = entityClass.getAnnotation(FileEntity.class);
        if (fileEntity != null && fileEntity instanceof FileEntity) {
            this.rowSize = ((FileEntity) fileEntity).rowSize();
            this.dumpRowSize = ((FileEntity) fileEntity).dumpRowSize();
        } else {
            throw new FileException("Entity must have FileEntity annotation");
        }
    }

    @Override
    public abstract T createEntity();

    @Override
    public Class<T> getEntityClass() {
        return entityClass;
    }

    @Override
    public String createRow(T entity) throws FileException {
        try {
            String returnString = createEmptyString(rowSize);
            for (String fieldName : stringMap.keySet()) {
                Object object = PropertyUtils.getProperty(entity, fieldName);
                String temp = object == null ? "" : String.valueOf(object);
                StringField stringField = stringMap.get(fieldName);
                temp = fillString(temp, stringField.fill(), stringField.textFloat(), stringField.size());
                returnString = replaceString(returnString, temp, stringField.position());
            }
            for (String fieldName : numberMap.keySet()) {
                Object object = PropertyUtils.getProperty(entity, fieldName);
                NumberField numberField = numberMap.get(fieldName);
                Number number = object == null ? null : object instanceof Number ? (Number) object : null;
                String temp;
                if (!numberField.convert()) {
                    temp = number == null ? "" : String.valueOf(number);
                } else {
                    temp = convertNumber(number, numberField.numberSize(), numberField.realSize(), true);
                }
                temp = fillString(temp, numberField.fill(), numberField.textFloat(), numberField.size());
                returnString = replaceString(returnString, temp, numberField.position());
            }
            for (String fieldName : dateMap.keySet()) {
                Object object = PropertyUtils.getProperty(entity, fieldName);
                Date tempDate = object == null ? null : object instanceof Date ? (Date) object : null;
                DateField dateField = dateMap.get(fieldName);
                String temp = convertDate(tempDate, dateField.pattern());
                returnString = replaceString(returnString, temp, dateField.position());
            }
            return returnString;
        } catch (IllegalAccessException ex) {
            throw new FileException(ex);
        } catch (NoSuchMethodException ex) {
            throw new FileException(ex);
        } catch (InvocationTargetException ex) {
            throw new FileException(ex);
        } catch (FileException ex) {
            throw new FileException(ex);
        }
    }

    @Override
    public T readRow(String row) throws FileException {
        // log.info("Read row sources initialized");
        //log.debug("Row = " + row);
        try {
            T entity = createEntity();
            for (String fieldName : stringMap.keySet()) {
                //log.debug("Field Name = " + fieldName);
                StringField stringField = stringMap.get(fieldName);
                String value = row.substring(stringField.position(), stringField.position() + stringField.size()).trim();
                // log.debug("Field Value = " + value);
                PropertyUtils.setProperty(entity, fieldName, value);
            }
            for (String fieldName : numberMap.keySet()) {
                //log.debug("Field Name = " + fieldName);
                NumberField numberField = numberMap.get(fieldName);
                Number value;
                String stringValue = row.substring(numberField.position(), numberField.position() + numberField.size()).trim();
                if (numberField.real()) {
                    value = Float.parseFloat(stringValue.replace(',', '.'));
                } else {
                    value = Integer.parseInt(stringValue);
                }
                // log.debug("Field Value = " + value);
                PropertyUtils.setProperty(entity, fieldName, value);
            }
            for (String fieldName : dateMap.keySet()) {
                // log.debug("Field Name = " + fieldName);
                DateField dateField = dateMap.get(fieldName);
                SimpleDateFormat format = new SimpleDateFormat(dateField.pattern());
                String stringValue = row.substring(dateField.position(), dateField.position() + dateField.size()).trim();
                Date value = format.parse(stringValue);
                //log.debug("Field Value = " + value);
                PropertyUtils.setProperty(entity, fieldName, value);
            }
            //log.info("Read row finished successfully");
            return entity;
        } catch (ParseException ex) {
            log.error(ex.getMessage(), ex);
            throw new FileException(ex);
        } catch (IllegalAccessException ex) {
            log.error(ex.getMessage(), ex);
            throw new FileException(ex);
        } catch (NoSuchMethodException ex) {
            log.error(ex.getMessage(), ex);
            throw new FileException(ex);
        } catch (InvocationTargetException ex) {
            log.error(ex.getMessage(), ex);
            throw new FileException(ex);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new FileException(ex);
        }
    }

    @Override
    public void writeToFile(List<T> entityList, String fileName, List<String> fileHeaders) throws FileException {
        FileWriter outfile = null;
        try {
            outfile = new FileWriter(new File(fileName));
            PrintWriter out = new PrintWriter(outfile);
            for (int i = 0; i < dumpRowSize; i++) {
                if (fileHeaders.get(i) != null) {
                    out.println(fileHeaders.get(i));
                } else {
                    out.println("");
                }
            }
            int rowCount = 0;
            for (T entity : entityList) {
                out.println(createRow(entity));
                rowCount++;
            }
            out.println("# ROWS COUNT : " + rowCount);
            out.flush();
            out.close();
        } catch (IOException ex) {
            throw new FileException(ex);
        } finally {
            try {
                outfile.close();
            } catch (IOException ex) {
            }
        }
    }

    @Override
    public List<T> readFromFile(File file) throws IOException, FileException {
        List<T> list = new ArrayList<T>();

        Reader reader = new FileReader(file);
        BufferedReader br = new BufferedReader(reader);
        String strLine;
        while ((strLine = br.readLine()) != null) {
            try {
                list.add(readRow(strLine));
            } catch (Exception ex) {
            }
        }
        return list;
    }

    private Map<String, StringField> findStringFields(Class<?> type) {
        Map<String, StringField> fileFields = new HashMap<String, StringField>();
        do {
            Field[] fields = type.getDeclaredFields();
            for (Field field : fields) {
                StringField stringField = field.getAnnotation(StringField.class);
                if (stringField != null) {
                    fileFields.put(field.getName(), stringField);
                }
            }
            type = type.getSuperclass();
        } while (type != Object.class);
        return fileFields;
    }

    private Map<String, NumberField> findNumberFields(Class<?> type) {
        Map<String, NumberField> numberFields = new HashMap<String, NumberField>();
        do {
            Field[] fields = type.getDeclaredFields();
            for (Field field : fields) {
                NumberField numberField = field.getAnnotation(NumberField.class);
                if (numberField != null) {
                    numberFields.put(field.getName(), numberField);
                }
            }
            type = type.getSuperclass();
        } while (type != Object.class);
        return numberFields;

    }

    private Map<String, DateField> findDateFields(Class<?> type) {
        Map<String, DateField> fileFields = new HashMap<String, DateField>();
        do {
            Field[] fields = type.getDeclaredFields();
            for (Field field : fields) {
                DateField dateField = field.getAnnotation(DateField.class);
                if (dateField != null) {
                    fileFields.put(field.getName(), dateField);
                }
            }
            type = type.getSuperclass();
        } while (type != Object.class);
        return fileFields;
    }

    private String createEmptyString(Integer size) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }

    private String fillString(String value, char filler, TextFloat textFloat, Integer size) throws FileException {
        StringBuilder text = new StringBuilder(value);
        if (text.length() >= size) {
            return text.toString();
        }
        if (textFloat.equals(TextFloat.LEFT)) {
            while (text.length() != size) {
                text.append(filler);
            }
            return text.toString();
        } else if (textFloat.equals(TextFloat.RIGHT)) {
            while (text.length() != size) {
                text.insert(0, filler);
            }
            return text.toString();
        } else {
            throw new FileException("Float Cannot be null");
        }
    }

    private String fillNumber(Number number, char filler, TextFloat textFloat, Integer size, Boolean real, Boolean seperator, int realSize, int numberSize) throws FileException {
        String returnText;
        Double doubleNumber;
        Integer integerNumber;

        if (real) {
            doubleNumber = number.doubleValue();
            returnText = this.convertDouble(doubleNumber, numberSize, realSize);

        } else {
            integerNumber = number.intValue();

            returnText = integerNumber.toString();

            if (returnText.length() >= size) {
                return returnText;
            }
            if (textFloat.equals(TextFloat.LEFT)) {
                while (returnText.length() != size) {
                    returnText = returnText + filler;
                }
            } else if (textFloat.equals(TextFloat.RIGHT)) {
                while (returnText.length() != size) {
                    returnText = filler + returnText;
                }
            } else {
                throw new FileException("Float Cannot be null");
            }
        }

        return returnText;
    }

    private String replaceString(String source, String newString, Integer position) {
        return source.substring(0, position) + newString + source.substring(position + newString.length());
    }

    private String convertDate(Date date, String pattern) throws FileException {
        try {
            if (date == null) {
                return "";
            }
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern(pattern);
            return sdf.format(date);

        } catch (Exception ex) {
            throw new FileException(ex);
        }
    }

    private String convertNumber(Number number, int numberSize, int realSize, boolean real) {
        if ((!real) || numberSize == 0) {
            return String.valueOf(number);
        }

        Double doubleNumber = number.doubleValue();
        String decimalPattern = "";
        String returnText;

        for (int i = 0; i < numberSize; i++) {
            decimalPattern += "#";
        }
        decimalPattern += ".";
        for (int i = 0; i < realSize; i++) {
            decimalPattern += "#";
        }

        DecimalFormat df = new DecimalFormat(decimalPattern);
        returnText = df.format(doubleNumber);



        while (returnText.length() != realSize + numberSize + 1) {
            returnText = '0' + returnText;
        }


        returnText = returnText.replace(",", ".");
        if (!returnText.contains(".")) {
            returnText += ".";
            for (int i = 0; i < realSize; i++) {
                returnText += "0";
            }

            returnText = returnText.substring(realSize + 1);
        }
        int size = returnText.substring(returnText.indexOf(".") + 1).length();
        if (size < realSize) {
            for (int i = size; i < realSize; i++) {
                returnText += "0";
            }

            returnText = returnText.substring(realSize - size);
        }

        return returnText;
    }

    private String convertDouble(Double doubleNumber, int numberSize, int realSize) {
        StringBuilder decimalPattern = new StringBuilder("");
        StringBuilder returnText;

        for (int i = 0; i < numberSize; i++) {
            decimalPattern.append("#");
        }
        decimalPattern.append(".");
        for (int i = 0; i < realSize; i++) {
            decimalPattern.append("#");
        }

        DecimalFormat df = new DecimalFormat(decimalPattern.toString());
        returnText = new StringBuilder(df.format(doubleNumber));


        while (returnText.length() != realSize + numberSize + 1) {
            returnText.insert(0, "0");
        }

        return returnText.toString().replace(",", ".");
    }
}
