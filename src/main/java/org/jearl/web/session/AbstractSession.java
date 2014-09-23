/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.web.session;

import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jearl.web.session.exception.SessionException;

/**
 *
 * @author extreme
 */
public abstract class AbstractSession implements Session {

    private static final Object THREAD_LOCK = new Object();
    private static final String SESSION = "SESSION";

    @Override
    public abstract javax.servlet.http.HttpSession getSession() throws SessionException;

    @Override
    public Map createMap() {
        return new ConcurrentHashMap();
    }

    @Override
    public void writeToSession(String key, Object myobject) throws SessionException {
        synchronized (THREAD_LOCK) {
            javax.servlet.http.HttpSession session = getSession();
            Map hashMap;
            Object temp = session.getAttribute(SESSION);
            if (temp != null && temp instanceof Map) {
                hashMap = (Map) temp;
            } else {
                hashMap = createMap();
            }
            hashMap.put(key, myobject);
            session.setAttribute(SESSION, hashMap);
        }
    }

    @Override
    public <Y extends Object> Y readFromSession(String key) throws SessionException {
        synchronized (THREAD_LOCK) {
            javax.servlet.http.HttpSession session = getSession();
            Map hashMap;
            Object temp = session.getAttribute(SESSION);
            if (temp != null && temp instanceof Map) {
                hashMap = (Map) temp;
            } else {
                return null;
            }
            temp = hashMap.get(key);
            if (temp == null) {
                return null;
            } else {
                return (Y) temp;
            }
        }
    }

    @Override
    public void deleteFromSession(String key) throws SessionException {
        synchronized (THREAD_LOCK) {
            javax.servlet.http.HttpSession session = getSession();
            Map hashMap;
            Object temp = session.getAttribute(SESSION);
            if (temp != null && temp instanceof Map) {
                hashMap = (Map) temp;
            } else {
                return;
            }
            hashMap.remove(key);
            session.setAttribute(SESSION, temp);
        }
    }

    @Override
    public void deleteAll() throws SessionException {
        synchronized (THREAD_LOCK) {
            javax.servlet.http.HttpSession session = getSession();
            session.removeAttribute(SESSION);
        }
    }

    @Override
    public void destroySession() throws SessionException {
        synchronized (THREAD_LOCK) {
            javax.servlet.http.HttpSession session = getSession();
            Enumeration i = session.getAttributeNames();
            while (i.hasMoreElements()) {
                Object temp = i.nextElement();
                if (temp != null) {
                    session.removeAttribute(temp.toString());
                }
            }
        }
    }

    @Override
    public <Y extends Object> Y getAttribute(String key) throws SessionException {
        synchronized (THREAD_LOCK) {
            javax.servlet.http.HttpSession session = getSession();
            Object temp = session.getAttribute(key);
            if (temp == null) {
                return null;
            } else {
                return (Y) temp;
            }
        }
    }

    @Override
    public void setAttribute(String key, Object value) throws SessionException {
        synchronized (THREAD_LOCK) {
            javax.servlet.http.HttpSession session = getSession();
            session.setAttribute(key, value);
        }
    }
}
