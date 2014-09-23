/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.sms.exception;

/**
 *
 * @author bamasyali
 */
public class SmsException extends Exception {

    public SmsException(Throwable cause) {
        super(cause);
    }

    public SmsException(String message, Throwable cause) {
        super(message, cause);
    }

    public SmsException(String message) {
        super(message);
    }

    public SmsException() {
    }
}
