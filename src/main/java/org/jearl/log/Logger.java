/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.log;

import org.apache.log4j.xml.DOMConfigurator;

/**
 *
 * @author bamasyali
 */
public final class Logger {

    static {
        DOMConfigurator.configureAndWatch("log4j.xml");   //log4j konfigürasyon dosyasının projedeki yerini veriyoruz.
    }

    private Logger() {
    }

    public static org.apache.log4j.Logger getLogger(Class c) {
        return org.apache.log4j.Logger.getLogger(c);
    }

    public static JearlLogger getJearlLogger(Class c) {
        return new JearlLoggerImpl(c);
    }
}
