/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.fileutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import org.jearl.fileutil.exception.FileException;
import org.jearl.fileutil.model.FileHashMethodEnum;

/**
 *
 * @author bamasyali
 */
public final class FileHasher {

    private FileHasher() {
    }

    public static String getHashFromFile(File file, FileHashMethodEnum method) throws FileException {
        try {
            MessageDigest md = MessageDigest.getInstance(method.getValue()); // SHA or MD5
            String hash = "";

            byte[] data = new byte[(int) file.length()];
            FileInputStream fis = new FileInputStream(file);
            fis.read(data);
            fis.close();

            md.update(data); // Reads it all at one go. Might be better to chunk it.

            byte[] digest = md.digest();

            for (int i = 0; i < digest.length; i++) {
                String hex = Integer.toHexString(digest[i]);
                if (hex.length() == 1) {
                    hex = "0" + hex;
                }
                hex = hex.substring(hex.length() - 2);
                hash += hex;
            }

            return hash;
        } catch (Exception ex) {
            throw new FileException(ex);
        }
    }

    public static String getHashFromFile(File file, FileHashMethodEnum method, Integer buff) throws FileException {
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "r");

            MessageDigest hashSum = MessageDigest.getInstance(method.getValue());

            byte[] buffer = new byte[buff];
            byte[] partialHash = null;

            long read = 0;
            long offset = file.length();
            int unitsize;

            while (read < offset) {
                unitsize = (int) (((offset - read) >= buff) ? buff : (offset - read));
                raf.read(buffer, 0, unitsize);
                hashSum.update(buffer, 0, unitsize);
                read += unitsize;
            }

            raf.close();
            partialHash = new byte[hashSum.getDigestLength()];
            partialHash = hashSum.digest();
            return partialHash.toString();
        } catch (Exception e) {
            throw new FileException(e);
        }
    }
}
