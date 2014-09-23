/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.web.session;

import java.io.Serializable;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.jearl.web.session.exception.SessionException;

/**
 *
 * @author extreme
 */
public class JsfSession extends AbstractSession implements Serializable{

    private ThreadLocal<javax.servlet.http.HttpSession> session;

    public JsfSession() {
        this.session = new ThreadLocal<javax.servlet.http.HttpSession>();
    }

    @Override
    public javax.servlet.http.HttpSession getSession() throws SessionException {
        if (session.get() == null) {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            session.set((javax.servlet.http.HttpSession) context.getSession(true));
        }
        return session.get();
    }

    public Integer getUserId() {
        try {
            return this.<Integer>readFromSession(SessionValue.USER_Id.getValue());
        } catch (SessionException ex) {
            return null;
        }
    }

    public Integer getCmpnyId() {
        try {
            return this.<Integer>readFromSession(SessionValue.CMPNY_Id.getValue());
        } catch (SessionException ex) {
            return null;
        }
    }

    public void setUserId(Integer userId) {
        try {
            writeToSession(SessionValue.USER_Id.getValue(), userId);
        } catch (SessionException ex) {
            return;
        }
    }

    public void setCmpnyId(Integer cmpnyId) {
        try {
            writeToSession(SessionValue.CMPNY_Id.getValue(), cmpnyId);
        } catch (SessionException ex) {
            return;
        }
    }

    public void deleteAllUserDataFromSession() {
        try {
            deleteFromSession(SessionValue.CMPNY_Id.getValue());
            deleteFromSession(SessionValue.USER_Id.getValue());
            deleteFromSession(SessionValue.USER.getValue());
            deleteFromSession(SessionValue.PERSON.getValue());
        } catch (SessionException ex) {
            return;
        }
    }

    private enum SessionValue {

        USER_Id("USER_Id"), USER("USER"), PERSON("PERSON"), CMPNY_Id("CMPNY_Id");
        private final String value;

        private SessionValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    };
};
