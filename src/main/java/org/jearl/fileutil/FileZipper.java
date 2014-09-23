/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.fileutil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.jearl.fileutil.exception.ZipException;

/**
 *
 * @author bamasyali
 */
public final class FileZipper {

    public static final Object THREAD_LOCK = new Object();

    private FileZipper() {
    }

    public static void doZip(File target, List<File> documents) throws ZipException {
        try {
            synchronized (THREAD_LOCK) {
                ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(target));
                for (File file : documents) {
                    zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
                    byte[] bs = FileByteConverter.getBytesFromFile(file);
                    zipOutputStream.write(bs, 0, bs.length);
                    zipOutputStream.closeEntry();
                }
                zipOutputStream.close();
            }
        } catch (Exception e) {
            throw new ZipException(e);
        }
    }
}
