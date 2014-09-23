/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ejb.zip;

import java.util.List;
import org.jearl.ejb.exception.ZipException;
import org.jearl.ejb.model.document.Document;

/**
 *
 * @author bamasyali
 */
public interface Zipper {

    byte[] doZip(List<Document> documents) throws ZipException;
}
