/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ldap.service.impl;

import org.jearl.ldap.service.LDapService;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import org.jearl.ldap.model.LdapProperties;
import org.jearl.logback.ErrorCategory;
import org.jearl.log.JearlLogger;
import org.jearl.log.Logger;

/**
 *
 * @author CanCobanoglu
 */
public class LDapServiceImpl implements LDapService{

    private final JearlLogger log;
    private final Hashtable env;
    private final LdapContext ldapContext;
    private final Control[] controls;

    public LDapServiceImpl(LdapProperties properties) throws NamingException {
        this.log = Logger.getJearlLogger(LDapServiceImpl.class);
        this.env = initContext(properties);
        this.controls = new Control[]{new FastBindControl()};
        this.ldapContext = new InitialLdapContext(env, controls);
        this.log.info("LdapFastBind created successfully.");
    }

    private Hashtable initContext(LdapProperties properties) {
        this.log.info("initContext sources initialized.");
        this.log.debug("properties = " + properties.toString());
        Hashtable temp = new Hashtable();
        temp.put(Context.INITIAL_CONTEXT_FACTORY, properties.getInitialContextFactory());
        temp.put(Context.SECURITY_AUTHENTICATION, properties.getSecurityAuthentication());
        temp.put(Context.PROVIDER_URL, properties.getProviderUrl());
        temp.put(Context.SECURITY_PRINCIPAL, properties.getSecurityPrincipal());
        temp.put(Context.SECURITY_CREDENTIALS, properties.getSecurityCredentials());
        this.log.info("initContext finished successfully.");
        return temp;
    }

    @Override
    public Boolean auteticate(String username, String password) {
        this.log.info("auteticate sources initialized.");
        this.log.debug("username = " + username + " , password = " + password);
        synchronized (this) {
            try {
                ldapContext.addToEnvironment(Context.SECURITY_PRINCIPAL, username);
                ldapContext.addToEnvironment(Context.SECURITY_CREDENTIALS, password);
                ldapContext.reconnect(controls);
                return true;
            } catch (Exception e) {
                log.error(e.getMessage(), ErrorCategory.WRONG_PARAMETER_ERROR, e);
                return false;
            } finally {
                this.log.info("auteticate finished successfully.");
            }
        }
    }

    @Override
    public List<SearchResult> search(String query) throws NamingException {
        this.log.info("search sources initialized.");
        this.log.debug("query = " + query);
        List<SearchResult> searchResults = new ArrayList<SearchResult>();
        NamingEnumeration namingEnumeration = ldapContext.search(query, null);
        if (namingEnumeration != null) {
            while (namingEnumeration.hasMoreElements()) {
                SearchResult sr = (SearchResult) namingEnumeration.next();
                searchResults.add(sr);
            }
        }
        this.log.info("search finished successfully.");
        return searchResults;
    }

    class FastBindControl implements Control {

        @Override
        public byte[] getEncodedValue() {
            return null;
        }

        @Override
        public String getID() {
            return "1.2.840.113556.1.4.1781";
        }

        @Override
        public boolean isCritical() {
            return true;
        }
    }
}
