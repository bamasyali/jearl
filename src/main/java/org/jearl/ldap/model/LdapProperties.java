/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ldap.model;

/**
 *
 * @author bamasyali
 */
public interface LdapProperties {

    String getInitialContextFactory();

    String getProviderUrl();

    String getSecurityAuthentication();

    String getSecurityCredentials();

    String getSecurityPrincipal();
}
