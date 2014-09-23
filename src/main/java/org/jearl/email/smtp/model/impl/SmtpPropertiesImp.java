/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.email.smtp.model.impl;

import org.jearl.email.smtp.model.SmtpProperties;

/**
 *
 * @author bamasyali
 */
public class SmtpPropertiesImp implements SmtpProperties {

    private final String host;
    private final String port;
    private final String username;
    private final String password;
    private final String ssl;
    private final String tls;
    private final String auth;

    public SmtpPropertiesImp(String host, String port, String username, String password, String ssl, String tls, String auth) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.ssl = ssl;
        this.tls = tls;
        this.auth = auth;
    }

    @Override
    public String getAuth() {
        return auth;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getPort() {
        return port;
    }

    @Override
    public String getSsl() {
        return ssl;
    }

    @Override
    public String getTls() {
        return tls;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
