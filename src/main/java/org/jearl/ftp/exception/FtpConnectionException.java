/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ftp.exception;

/**
 *
 * @author PC
 */
public class FtpConnectionException extends Exception {

    public FtpConnectionException(Throwable cause) {
        super(cause);
    }

    public FtpConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public FtpConnectionException(String message) {
        super(message);
    }

    public FtpConnectionException() {
    }
}
