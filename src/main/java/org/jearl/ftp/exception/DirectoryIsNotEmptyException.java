/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ftp.exception;

/**
 *
 * @author bamasyali
 */
public class DirectoryIsNotEmptyException extends Exception {

    public DirectoryIsNotEmptyException(Throwable cause) {
        super(cause);
    }

    public DirectoryIsNotEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

    public DirectoryIsNotEmptyException(String message) {
        super(message);
    }

    public DirectoryIsNotEmptyException() {
    }
}
