/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.email.lotus.model.impl;

import org.jearl.email.lotus.model.LotusProperties;

/**
 *
 * @author bamasyali
 */
public class LotusPropertiesImp implements LotusProperties {

    private final String hostName;// = "TRUSERZ02";
    private final String userName;// = "Notes_Senkronizasyon";
    private final String password;// = "Ns123456";
    private final String database;// = "names_.nsf";

    public LotusPropertiesImp(String hostName, String userName, String password, String database) {
        this.hostName = hostName;
        this.userName = userName;
        this.password = password;
        this.database = database;
    }

    @Override
    public String getDatabase() {
        return database;
    }

    @Override
    public String getHostName() {
        return hostName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUserName() {
        return userName;
    }
}
