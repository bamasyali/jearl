/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.socket.model.impl;

import org.jearl.socket.model.JSocketPropertie;

/**
 *
 * @author bamasyali
 */
public class JSocketPropertieImp implements JSocketPropertie {

    private final String host;
    private final Integer port;

    public JSocketPropertieImp(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public Integer getPort() {
        return port;
    }
}
