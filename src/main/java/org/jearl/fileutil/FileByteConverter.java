/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.fileutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.processing.FilerException;

/**
 *
 * @author bamasyali
 */
public final class FileByteConverter {

    private static Map<String, Object> map;
    private static final Object THREAD_LOCK = new Object();

    static {
        map = new ConcurrentHashMap<String, Object>();
    }

    private FileByteConverter() {
    }

    public static byte[] getBytesFromFile(File file) throws IOException {
        String fileName;
        synchronized (THREAD_LOCK) {
            fileName = file.getPath();
            if (map.get(fileName) == null) {
                map.put(fileName, new Object());
            }
        }
        synchronized (map.get(fileName)) {
            InputStream is = new FileInputStream(file);
            long length = file.length();
            if (length > Long.MAX_VALUE) {
                throw new FilerException("File is too large");
            }

            byte[] bytes = new byte[(int) length];

            // Read in the bytes
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }

            // Ensure all the bytes have been read in
            if (offset < bytes.length) {
                throw new IOException("Could not completely read file " + file.getName());
            }

            // Close the input stream and return bytes
            is.close();
            return bytes;
        }
    }
}
