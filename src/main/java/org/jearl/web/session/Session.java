/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.web.session;

import java.util.Map;
import org.jearl.web.session.exception.SessionException;

/**
 *
 * @author bamasyali
 */
public interface Session {

    javax.servlet.http.HttpSession getSession() throws SessionException;

    Map createMap();

    void writeToSession(String key, Object myobject) throws SessionException;

    <Y extends Object> Y readFromSession(String key) throws SessionException;

    void deleteFromSession(String key) throws SessionException;

    void deleteAll() throws SessionException;

    void destroySession() throws SessionException;

    <Y extends Object> Y getAttribute(String key) throws SessionException;

    void setAttribute(String key, Object value) throws SessionException;
}
