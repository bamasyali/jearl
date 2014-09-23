/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.fileutil.exception;

/**
 *
 * @author bamasyali
 */
public class FileException extends Exception {

    public FileException(Throwable cause) {
        super(cause);
    }

    public FileException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileException(String message) {
        super(message);
    }

    public FileException() {
    }
}
