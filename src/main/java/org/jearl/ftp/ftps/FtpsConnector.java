/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ftp.ftps;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPSClient;
import org.jearl.ftp.FtpConnector;
import org.jearl.ftp.standart.FtpConnectorImp;

/**
 *
 * @author PC
 */
public class FtpsConnector extends FtpConnectorImp implements FtpConnector {

    public FtpsConnector() {
        //super(bufferSize);
    }

    @Override
    public FTPClient getFTPClient() {
        return new FTPSClient();
    }
}
