/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ejb.exception;

/**
 *
 * @author bamasyali
 */
public class ReportException extends Exception {

    public ReportException(Throwable cause) {
        super(cause);
    }

    public ReportException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReportException(String message) {
        super(message);
    }

    public ReportException() {
    }
}
