/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ftp.model.impl;

import org.jearl.ftp.model.FtpFile;

/**
 *
 * @author PC
 */
public class FtpFileImpl implements FtpFile {

    private final String directory;
    private final String fileName;

    public FtpFileImpl(String directory, String fileName) {
        this.directory = directory;
        this.fileName = fileName;
    }

    @Override
    public String getDirectory() {
        return directory;
    }

    @Override
    public String getFileName() {
        return fileName;
    }
}
