/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ejb.exception;

/**
 *
 * @author bamasyali
 */
public class ZipException extends Exception {

    public ZipException(Throwable cause) {
        super(cause);
    }

    public ZipException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZipException(String message) {
        super(message);
    }

    public ZipException() {
    }
}
