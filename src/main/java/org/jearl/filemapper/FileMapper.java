/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.filemapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.jearl.filemapper.exception.FileException;

/**
 *
 * @author nodeser
 */
public interface FileMapper<T> {

    T createEntity();

    Class<T> getEntityClass();

    String createRow(T entity) throws FileException;

    T readRow(String row) throws FileException;

    void writeToFile(List<T> entityList, String fileName, List<String> fileHeaders) throws FileException;

    List<T> readFromFile(File file) throws IOException, FileException;
}
