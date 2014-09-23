/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.email.exception;

/**
 *
 * @author bamasyali
 */
public class EMailException extends Exception {

    public EMailException(Throwable cause) {
        super(cause);
    }

    public EMailException(String message, Throwable cause) {
        super(message, cause);
    }

    public EMailException(String message) {
        super(message);
    }

    public EMailException() {
    }
}
