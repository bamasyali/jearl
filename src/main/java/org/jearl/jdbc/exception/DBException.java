/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.jdbc.exception;

/**
 *
 * @author bamasyali
 */
public class DBException extends Exception {

    public DBException(Throwable cause) {
        super(cause);
    }

    public DBException(String message, Throwable cause) {
        super(message, cause);
    }

    public DBException(String message) {
        super(message);
    }

    public DBException() {
    }
}
