/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.email.smtp.model;

import java.io.Serializable;
import org.jearl.email.model.EMailProperties;

/**
 *
 * @author bamasyali
 */
public interface SmtpProperties extends EMailProperties, Serializable {

    String getAuth();

    String getHost();

    String getPassword();

    String getPort();

    String getSsl();

    String getTls();

    String getUsername();
}
