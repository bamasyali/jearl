/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.web.jsf.message;

/**
 *
 * @author bamasyali
 */
public final class Message {

    private final String header;
    private final String content;
    private Exception exception;
    private Boolean isShowException;

    public Message(String header, String content) {
        this.header = header;
        this.content = content;
        this.exception = null;
        this.isShowException = false;
    }

    public Message(String header, String content, Boolean isShowException) {
        this.header = header;
        this.content = content;
        this.exception = null;
        this.isShowException = isShowException;
    }

    public Message(String header, String content, Exception exception, Boolean isShowException) {
        this.header = header;
        this.content = content;
        this.exception = exception;
        this.isShowException = isShowException;
    }

    public String getContent() {
        if (isShowException && exception != null) {
            return content + " " + exception.getMessage();
        } else {
            return content;
        }
    }

    public String getHeader() {
        return header;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public Boolean getIsShowException() {
        return isShowException;
    }

    public void setIsShowException(Boolean isShowException) {
        this.isShowException = isShowException;
    }
}
