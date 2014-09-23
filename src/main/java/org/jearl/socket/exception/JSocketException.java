/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.socket.exception;

/**
 *
 * @author bamasyali
 */
public class JSocketException extends Exception {

    public JSocketException(Throwable cause) {
        super(cause);
    }

    public JSocketException(String message, Throwable cause) {
        super(message, cause);
    }

    public JSocketException(String message) {
        super(message);
    }

    public JSocketException() {
    }
}
