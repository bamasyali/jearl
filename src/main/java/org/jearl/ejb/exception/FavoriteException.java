/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ejb.exception;

/**
 *
 * @author bamasyali
 */
public class FavoriteException extends Exception {

    public FavoriteException(Throwable cause) {
        super(cause);
    }

    public FavoriteException(String message, Throwable cause) {
        super(message, cause);
    }

    public FavoriteException(String message) {
        super(message);
    }

    public FavoriteException() {
    }
}
