/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.email;

import java.util.List;
import org.jearl.email.exception.EMailException;
import org.jearl.email.model.EMail;
import org.jearl.email.model.EMailLogger;
import org.jearl.email.model.EMailProperties;

/**
 *
 * @author bamasyali
 */
public interface EMailSender<T extends EMailProperties> {

    void sendMail(T properties, EMail email, Boolean isThreaded) throws EMailException;

    void sendMail(T properties, List<EMail> emails) throws EMailException;

    void sendMail(T properties, List<EMail> emails, Integer threadNumber) throws EMailException;

    void log(EMailLogger logger, List<EMail> emails);
}
