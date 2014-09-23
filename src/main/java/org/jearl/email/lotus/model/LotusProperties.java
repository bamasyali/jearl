/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.email.lotus.model;

import org.jearl.email.model.EMailProperties;

/**
 *
 * @author bamasyali
 */
public interface LotusProperties extends EMailProperties {

    String getDatabase();

    String getHostName();

    String getPassword();

    String getUserName();
}
