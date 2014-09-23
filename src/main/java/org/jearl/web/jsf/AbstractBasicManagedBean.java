/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.web.jsf;

import java.io.IOException;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import org.jearl.logback.ErrorCategory;
import org.jearl.logback.Log;
import org.jearl.logback.LogService;
import org.jearl.web.jsf.message.Message;
import org.jearl.web.session.JsfSession;

/**
 *
 * @author amasyalim
 */
public abstract class AbstractBasicManagedBean implements Serializable {

    private static final String CONTAINER = "JEARL";
    private static final Log logger = new LogService(AbstractBasicManagedBean.class);
    private static final String ERROR_MESSAGE = "Error";
    protected static final String FACES_REDIRECT = "?faces-redirect=true;";
    protected final JsfSession session;
    protected final Integer userId;
    protected final Integer cmpnyId;
    protected final String userIp;

    public AbstractBasicManagedBean() {
        this.session = new JsfSession();
        this.userId = session.getUserId();
        this.cmpnyId = session.getCmpnyId();
        this.userIp = ((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteHost();
    }

    protected final <Y extends Object> Y getManagedBean(Class<Y> c) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return (Y) facesContext.getApplication().getVariableResolver().resolveVariable(facesContext, c.getSimpleName());
    }

    protected final void redirectPage(String url) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(url);
        } catch (IOException ex) {
            logger.error(AbstractBasicManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
        }
    }

    protected final void setInfo(String header, String contend) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, header != null ? header : ERROR_MESSAGE, contend != null ? contend : ERROR_MESSAGE));
    }

    protected final void setInfo(Message message) {
        if (message == null) {
            return;
        }
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message.getHeader(), message.getContent()));
    }

    protected final void setWarning(String header, String contend) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, header != null ? header : ERROR_MESSAGE, contend != null ? contend : ERROR_MESSAGE));
    }

    protected final void setWarning(Message message) {
        if (message == null) {
            return;
        }
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, message.getHeader(), message.getContent()));
    }

    protected final void setWarning(Message message, Exception ex) {
        if (message == null) {
            return;
        }
        message.setException(ex);
        setWarning(message);
    }

    protected final void setError(String header, String contend) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, header != null ? header : ERROR_MESSAGE, contend != null ? contend : ERROR_MESSAGE));
    }

    protected final void setError(Message message) {
        if (message == null) {
            return;
        }
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message.getHeader(), message.getContent()));
    }

    protected final void setError(Message message, Exception ex) {
        if (message == null) {
            return;
        }
        message.setException(ex);
        setError(message);
    }

    protected final void setFatalError(String header, String contend) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, header != null ? header : ERROR_MESSAGE, contend != null ? contend : ERROR_MESSAGE));
    }

    protected final void setFatalError(Message message, Exception ex) {
        if (message == null) {
            return;
        }
        message.setException(ex);
        setFatalError(message);
    }

    protected final void setFatalError(Message message) {
        if (message == null) {
            return;
        }
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, message.getHeader(), message.getContent()));
    }

    protected final Integer getCmpnyId() {
        return cmpnyId;
    }

    protected final JsfSession getSession() {
        return session;
    }

    protected final Integer getUserId() {
        return userId;
    }

    protected final String getUserIp() {
        return userIp;
    }
}
