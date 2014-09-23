/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ftp.exception;

/**
 *
 * @author PC
 */
public class FileAlreadyExistsException extends Exception {

    public FileAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public FileAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileAlreadyExistsException(String message) {
        super(message);
    }

    public FileAlreadyExistsException() {
    }
}
