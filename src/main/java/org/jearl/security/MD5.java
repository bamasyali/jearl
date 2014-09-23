// bu dosya da amacımız logın
package org.jearl.security;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class MD5 {

    private MD5() {
    }

    public static String getHashString(String s) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] bytesOfMessage = s.getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(bytesOfMessage);
        BigInteger bigInt = new BigInteger(1, digest);
        return bigInt.toString(16);
    }
}
