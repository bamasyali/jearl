/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ftp.standart;

import java.io.*;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.jearl.ftp.FtpConnector;
import org.jearl.ftp.exception.DirectoryIsNotEmptyException;
import org.jearl.ftp.exception.FileAlreadyExistsException;
import org.jearl.ftp.exception.FileDoesNotExistsException;
import org.jearl.ftp.exception.FtpConnectionException;
import org.jearl.ftp.model.FtpFile;
import org.jearl.ftp.model.FtpPropertie;
import org.jearl.log.Logger;

/**
 *
 * @author PC
 */
public class FtpConnectorImp implements FtpConnector {

    //private final Integer bufferSize;

    public FtpConnectorImp() {
        //this.bufferSize = bufferSize;
    }

    public FTPClient getFTPClient() {
        return new FTPClient();
    }

    private Boolean getFileExists(FTPClient client, String fileName) throws IOException {
        FTPFile[] ftpFiles = client.listFiles();
        String flName = null;
        for (FTPFile fTPFile : ftpFiles) {
            if (fTPFile.getName().equals(fileName)) {
                flName = fTPFile.getName();
            }
        }
        return flName != null;
    }

    @Override
     public void transfer(FtpPropertie propertie1, FtpFile file1, FtpPropertie propertie2, FtpFile file2, Boolean forceTransfer) throws FtpConnectionException, FileAlreadyExistsException, FileDoesNotExistsException{
        FTPClient ftp1 = getFTPClient();
        FTPClient ftp2 = getFTPClient();
        FileInputStream fis = null;

        try {
            ftp1.connect(propertie1.getHost());
            ftp1.login(propertie1.getUser(), propertie1.getPassword());

            ftp2.connect(propertie2.getHost());
            ftp2.login(propertie2.getUser(), propertie2.getPassword());

            if (!ftp1.changeWorkingDirectory(file1.getDirectory())) {
                throw new FileDoesNotExistsException("Directory does not exist..");
            }

            if (!getFileExists(ftp1, file1.getFileName())) {
                throw new FileDoesNotExistsException("File does not exist..");
            }

            fis = new FileInputStream(file1.getFileName());

            if (!ftp2.changeWorkingDirectory(file2.getDirectory())) {
                throw new FileDoesNotExistsException("Directory does not exist..");
            }

            if (getFileExists(ftp2, file2.getFileName())) {
                if (forceTransfer) {
                    ftp2.storeFile(file2.getFileName(), fis);
                } else {
                    throw new FileAlreadyExistsException("File is already exist..");
                }
            } else {
                ftp2.storeFile(file2.getFileName(), fis);
            }
        } catch (FileDoesNotExistsException e) {
            Logger.getLogger(FtpConnectorImp.class).error(e, e);
            throw new FileDoesNotExistsException(e);
        } catch (FileAlreadyExistsException e) {
            Logger.getLogger(FtpConnectorImp.class).error(e, e);
            throw new FileAlreadyExistsException(e);
        } catch (IOException e) {
            Logger.getLogger(FtpConnectorImp.class).error(e, e);
            throw new FtpConnectionException(e);
        } catch (Exception e) {
            Logger.getLogger(FtpConnectorImp.class).error(e, e);
            throw new FtpConnectionException(e);
        } finally {
            try {
                ftp2.logout();
                ftp2.disconnect();
                ftp1.logout();
                ftp1.disconnect();
            } catch (Exception ex) {
                throw new FtpConnectionException(ex);
            }
        }
    }

    @Override
    public void transfer(FtpPropertie propertie, FtpFile file1, File file2, Boolean forceTransfer) throws FtpConnectionException, FileAlreadyExistsException, FileDoesNotExistsException {
        FTPClient ftp = getFTPClient();

        try {
            ftp.connect(propertie.getHost());
            ftp.login(propertie.getUser(), propertie.getPassword());

            if (!ftp.changeWorkingDirectory(file1.getDirectory())) {
                throw new FileDoesNotExistsException("Directory does not exist..");
            }

            if (!getFileExists(ftp, file1.getFileName())) {
                throw new FileDoesNotExistsException("File does not exist..");
            }

            if (file2.exists()) {
                if (forceTransfer) {
                    OutputStream os = new FileOutputStream(file2);
                    //BufferedOutputStream bos = new BufferedOutputStream(os, bufferSize);
                    ftp.retrieveFile(file1.getFileName(), os);
                } else {
                    throw new FileAlreadyExistsException("File is already exist..");
                }
            } else {
                OutputStream os = new FileOutputStream(file2);
              //  BufferedOutputStream bos = new BufferedOutputStream(os, bufferSize);
                ftp.retrieveFile(file1.getFileName(), os);
            }

        } catch (FileDoesNotExistsException e) {
            Logger.getLogger(FtpConnectorImp.class).error(e, e);
            throw new FileDoesNotExistsException(e);
        } catch (FileAlreadyExistsException e) {
            Logger.getLogger(FtpConnectorImp.class).error(e, e);
            throw new FileAlreadyExistsException(e);
        } catch (IOException e) {
            Logger.getLogger(FtpConnectorImp.class).error(e, e);
            throw new FtpConnectionException(e);
        } catch (Exception e) {
            Logger.getLogger(FtpConnectorImp.class).error(e, e);
            throw new FtpConnectionException(e);
        } finally {
            try {
                ftp.logout();
                ftp.disconnect();
            } catch (Exception ex) {
                throw new FtpConnectionException(ex);
            }
        }
    }

    @Override
    public void transfer(File file1, FtpPropertie propertie, FtpFile file2, Boolean forceTransfer) throws FtpConnectionException, FileAlreadyExistsException, FileDoesNotExistsException {
        FTPClient ftp = getFTPClient();
        FileInputStream fis = null;

        try {

            ftp.connect(propertie.getHost());
            ftp.login(propertie.getUser(), propertie.getPassword());

            if (file1.exists()) {
                fis = new FileInputStream(file1);
            } else {
                throw new FileDoesNotExistsException("file does not exist..");
            }

            if (!ftp.changeWorkingDirectory(file2.getDirectory())) {
                throw new FileDoesNotExistsException("Directory does not exist..");
            }


            if (getFileExists(ftp, file2.getFileName())) {
                if (forceTransfer) {
                    ftp.storeFile(file2.getFileName(), fis);
                } else {
                    throw new FileAlreadyExistsException("File is already exist..");
                }
            } else {
                ftp.storeFile(file2.getFileName(), fis);
            }
        } catch (FileDoesNotExistsException e) {
            Logger.getLogger(FtpConnectorImp.class).error(e, e);
            throw new FileDoesNotExistsException(e);
        } catch (FileAlreadyExistsException e) {
            Logger.getLogger(FtpConnectorImp.class).error(e, e);
            throw new FileAlreadyExistsException(e);
        } catch (IOException e) {
            Logger.getLogger(FtpConnectorImp.class).error(e, e);
            throw new FtpConnectionException(e);
        } catch (Exception e) {
            Logger.getLogger(FtpConnectorImp.class).error(e, e);
            throw new FtpConnectionException(e);
        } finally {
            try {
                fis.close();
                ftp.logout();
                ftp.disconnect();
            } catch (Exception ex) {
                throw new FtpConnectionException(ex);
            }
        }
    }

    @Override
    public void removeFile(FtpPropertie propertie, FtpFile file) throws FtpConnectionException, FileDoesNotExistsException {
        FTPClient ftp = getFTPClient();

        try {
            ftp.connect(propertie.getHost());
            ftp.login(propertie.getUser(), propertie.getPassword());

            if (!ftp.changeWorkingDirectory(file.getDirectory())) {
                throw new FileDoesNotExistsException("Directory does not exist..");
            }

            if (getFileExists(ftp, file.getFileName())) {
                ftp.deleteFile(file.getFileName());
            } else {
                throw new FileDoesNotExistsException("File does not exist..");
            }
        } catch (FileDoesNotExistsException e) {
            Logger.getLogger(FtpConnectorImp.class).error(e, e);
            throw new FileDoesNotExistsException(e);
        } catch (IOException e) {
            Logger.getLogger(FtpConnectorImp.class).error(e, e);
            throw new FtpConnectionException(e);
        } catch (Exception e) {
            Logger.getLogger(FtpConnectorImp.class).error(e, e);
            throw new FtpConnectionException(e);
        } finally {
            try {
                ftp.logout();
                ftp.disconnect();
            } catch (Exception ex) {
                throw new FtpConnectionException(ex);
            }
        }
    }

    @Override
    public void removeDirectory(FtpPropertie propertie, FtpFile file, Boolean deleteCascade) throws FtpConnectionException, FileDoesNotExistsException, DirectoryIsNotEmptyException {
        FTPClient ftp = getFTPClient();

        try {
            ftp.connect(propertie.getHost());
            ftp.login(propertie.getUser(), propertie.getPassword());

            if (!ftp.changeWorkingDirectory(file.getDirectory())) {
                throw new FileDoesNotExistsException("Directory does not exist..");
            }

            FTPFile[] files = ftp.listFiles();

            if (files.length > 0) {
                if (deleteCascade) {
                    for (FTPFile fTPFile : files) {
                        ftp.deleteFile(fTPFile.getName());
                    }
                    ftp.removeDirectory(file.getDirectory());
                } else {
                    ftp.logout();
                    ftp.disconnect();
                    throw new DirectoryIsNotEmptyException("Directory is not empty");
                }
            } else {
                ftp.removeDirectory(file.getDirectory());
            }


        } catch (FileDoesNotExistsException e) {
            Logger.getLogger(FtpConnectorImp.class).error(e, e);
            throw new FileDoesNotExistsException(e);
        } catch (DirectoryIsNotEmptyException e) {
            Logger.getLogger(FtpConnectorImp.class).error(e, e);
            throw new DirectoryIsNotEmptyException(e);
        } catch (IOException e) {
            Logger.getLogger(FtpConnectorImp.class).error(e, e);
            throw new FtpConnectionException(e);
        } catch (Exception e) {
            Logger.getLogger(FtpConnectorImp.class).error(e, e);
            throw new FtpConnectionException(e);
        } finally {
            try {
                ftp.logout();
                ftp.disconnect();
            } catch (Exception ex) {
                throw new FtpConnectionException(ex);
            }
        }
    }

    @Override
    public void makeDirectory(FtpPropertie propertie, FtpFile file) throws FtpConnectionException, FileAlreadyExistsException {
        FTPClient ftp = getFTPClient();

        try {
            ftp.connect(propertie.getHost());
            ftp.login(propertie.getUser(), propertie.getPassword());

            if (ftp.changeWorkingDirectory(file.getDirectory())) {
                throw new FileAlreadyExistsException("Directory is already exist..");
            } else {
                ftp.makeDirectory(file.getDirectory());
            }

        } catch (IOException e) {
            Logger.getLogger(FtpConnectorImp.class).error(e, e);
            throw new FtpConnectionException(e);
        } catch (Exception e) {
            Logger.getLogger(FtpConnectorImp.class).error(e, e);
            throw new FtpConnectionException(e);
        } finally {
            try {
                ftp.logout();
                ftp.disconnect();
            } catch (Exception ex) {
                throw new FtpConnectionException(ex);
            }
        }
    }
}
