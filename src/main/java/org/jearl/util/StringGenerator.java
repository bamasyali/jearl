/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.util;

import java.util.Random;

/**
 *
 * @author amasyalim
 */
public final class StringGenerator {

    private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static Random rnd = new Random();

    private StringGenerator() {
    }

    public static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.toCharArray()[rnd.nextInt(AB.length())]);
        }
        return sb.toString();
    }
}
