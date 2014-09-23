/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ftp;

import java.io.File;
import org.jearl.ftp.exception.DirectoryIsNotEmptyException;
import org.jearl.ftp.exception.FileAlreadyExistsException;
import org.jearl.ftp.exception.FileDoesNotExistsException;
import org.jearl.ftp.exception.FtpConnectionException;
import org.jearl.ftp.model.FtpFile;
import org.jearl.ftp.model.FtpPropertie;

/**
 *
 * @author PC
 */
public interface FtpConnector {

    void transfer(FtpPropertie propertie1, FtpFile file1, FtpPropertie propertie2, FtpFile file2, Boolean forceTransfer) throws FtpConnectionException, FileAlreadyExistsException, FileDoesNotExistsException;

    void transfer(FtpPropertie propertie, FtpFile file1, File file2, Boolean forceTransfer) throws FtpConnectionException, FileAlreadyExistsException, FileDoesNotExistsException;

    void transfer(File file1, FtpPropertie propertie, FtpFile file2, Boolean forceTransfer) throws FtpConnectionException, FileAlreadyExistsException, FileDoesNotExistsException;

    void removeFile(FtpPropertie propertie, FtpFile file) throws FtpConnectionException, FileDoesNotExistsException;

    void removeDirectory(FtpPropertie propertie, FtpFile file, Boolean deleteCascade) throws FtpConnectionException, FileDoesNotExistsException, DirectoryIsNotEmptyException;

    void makeDirectory(FtpPropertie propertie, FtpFile file) throws FtpConnectionException, FileAlreadyExistsException;
}
