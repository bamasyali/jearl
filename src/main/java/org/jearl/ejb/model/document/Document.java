/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ejb.model.document;

import org.jearl.ejb.model.Entity;

/**
 *
 * @author bamasyali
 */
public interface Document<USER> extends Entity<Integer, USER> {

    Integer getDocId();

    void setDocId(Integer id);

    String getDocTable();

    void setDocTable(String table);

    int getDocRecord();

    void setDocRecord(int record);

    String getDocFilename();

    void setDocFilename(String filename);

    byte[] getDocFile();

    void setDocFile(byte[] file);
}
