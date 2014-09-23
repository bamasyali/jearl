/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.web.session.exception;

/**
 *
 * @author bamasyali
 */
public class SessionException extends Exception {

    public SessionException(Throwable cause) {
        super(cause);
    }

    public SessionException(String message, Throwable cause) {
        super(message, cause);
    }

    public SessionException(String message) {
        super(message);
    }

    public SessionException() {
    }
}
