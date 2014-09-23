/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ftp.model.impl;

import org.jearl.ftp.model.FtpPropertie;

/**
 *
 * @author PC
 */
public class FtpPropertieImpl implements FtpPropertie {

    private final String host;
    private final Integer port;
    private final String user;
    private final String password;

    public FtpPropertieImpl(String host, Integer port, String user, String password) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
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
    public Integer getPort() {
        return port;
    }

    @Override
    public String getUser() {
        return user;
    }
}
