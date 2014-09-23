/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ftp.exception;

/**
 *
 * @author PC
 */
public class FileDoesNotExistsException extends Exception {

    public FileDoesNotExistsException(Throwable cause) {
        super(cause);
    }

    public FileDoesNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileDoesNotExistsException(String message) {
        super(message);
    }

    public FileDoesNotExistsException() {
    }
}
