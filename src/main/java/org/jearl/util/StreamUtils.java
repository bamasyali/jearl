/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.util;

import java.io.*;

/**
 *
 * @author PC
 */
public final class StreamUtils {

    private StreamUtils() {
    }

    public static void transfer(InputStream is, OutputStream os, Integer bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];
        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedOutputStream bos = new BufferedOutputStream(os);
        int readCount;
        while ((readCount = bis.read(buffer)) > 0) {
            bos.write(buffer, 0, readCount);
        }
        bis.close();
        bos.close();
    }

    public static String convertInputStreamToString(InputStream is) throws IOException {
        byte[] bytes = new byte[is.available()];
        is.read(bytes);
        return new String(bytes, "UTF-8");
    }
}
