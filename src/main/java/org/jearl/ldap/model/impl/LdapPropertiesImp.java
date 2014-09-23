/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ldap.model.impl;

import org.jearl.ldap.model.LdapProperties;

/**
 *
 * @author bamasyali
 */
public class LdapPropertiesImp implements LdapProperties {

    private final String initialContextFactory;
    private final String securityAuthentication;
    private final String providerUrl;
    private final String securityPrincipal;
    private final String securityCredentials;

    public LdapPropertiesImp(String initialContextFactory, String securityAuthentication, String providerUrl, String securityPrincipal, String securityCredentials) {
        this.initialContextFactory = initialContextFactory;
        this.securityAuthentication = securityAuthentication;
        this.providerUrl = providerUrl;
        this.securityPrincipal = securityPrincipal;
        this.securityCredentials = securityCredentials;
    }

    @Override
    public String getInitialContextFactory() {
        return initialContextFactory;
    }

    @Override
    public String getProviderUrl() {
        return providerUrl;
    }

    @Override
    public String getSecurityAuthentication() {
        return securityAuthentication;
    }

    @Override
    public String getSecurityCredentials() {
        return securityCredentials;
    }

    @Override
    public String getSecurityPrincipal() {
        return securityPrincipal;
    }

    @Override
    public String toString() {
        return "LdapPropertiesImp{" + "initialContextFactory=" + initialContextFactory + ", securityAuthentication=" + securityAuthentication + ", providerUrl=" + providerUrl + ", securityPrincipal=" + securityPrincipal + ", securityCredentials=" + securityCredentials + '}';
    }
}
