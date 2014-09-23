/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ejb.zip;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.jearl.ejb.exception.ZipException;
import org.jearl.ejb.model.document.Document;
import org.jearl.fileutil.FileByteConverter;

/**
 *
 * @author bamasyali
 */
public class ZipperImpl implements Zipper {

    public static final Object THREAD_LOCK = new Object();
    private static final String FILE_NAME = "temp.zip";

    @Override
    public byte[] doZip(List<Document> documents) throws ZipException {
        try {
            synchronized (THREAD_LOCK) {
                try {
                    File file = new File(FILE_NAME);
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
                    for (Document document : documents) {
                        zipOutputStream.putNextEntry(new ZipEntry(document.getDocFilename()));
                        zipOutputStream.write(document.getDocFile(), 0, document.getDocFile().length);
                        zipOutputStream.closeEntry();
                    }
                    zipOutputStream.close();
                    byte[] bs = FileByteConverter.getBytesFromFile(file);

                    if (file.exists()) {
                        file.delete();
                    }
                    return bs;
                } catch (Exception ex) {
                    throw new ZipException(ex);
                }
            }
        } catch (Exception e) {
            throw new ZipException(e);
        }
    }
}
