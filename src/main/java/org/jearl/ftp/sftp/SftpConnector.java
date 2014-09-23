/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ftp.sftp;

import com.jcraft.jsch.*;
import java.io.*;
import java.util.List;
import org.jearl.ftp.FtpConnector;
import org.jearl.ftp.exception.FileAlreadyExistsException;
import org.jearl.ftp.exception.FileDoesNotExistsException;
import org.jearl.ftp.exception.FtpConnectionException;
import org.jearl.ftp.model.FtpFile;
import org.jearl.ftp.model.FtpPropertie;
import org.jearl.log.Logger;
import org.jearl.util.StreamUtils;

/**
 *
 * @author PC
 */
public class SftpConnector implements FtpConnector {

    private final Integer bufferSize;

    public SftpConnector(Integer bufferSize) {
        this.bufferSize = bufferSize;
    }

    private class SftpConnection {

        private Session session;
        private Channel channel;
        private ChannelSftp channelSftp;
        private JSch jsch;
        private Boolean directoryExist;
        private Boolean directoryEmpty;
        private Boolean fileExist;
        private List files;

        public SftpConnection(FtpPropertie propertie, FtpFile file) throws JSchException, SftpException {
            this.jsch = new JSch();
            this.session = jsch.getSession(propertie.getUser(), propertie.getHost(), propertie.getPort());
            this.session.setPassword(propertie.getPassword());
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            channel = session.openChannel("sftp");
            channel.connect();
            channelSftp = (ChannelSftp) channel;

            String parentDirectory = getParentDirectory(file.getDirectory());
            String directoryName = getDirectoryName(file.getDirectory());
            this.getChannelSftp().cd(parentDirectory);


            this.directoryExist = false;
            this.fileExist = false;

            final List parentFiles = this.getChannelSftp().ls(parentDirectory);

            if (parentFiles.isEmpty()) {
                return;
            }

            for (int i = 0; i < parentFiles.size(); i++) {
                ChannelSftp.LsEntry lsEntry = (ChannelSftp.LsEntry) parentFiles.get(i);
                if (!lsEntry.getFilename().equals(".") && !lsEntry.getFilename().equals("..")) {
                    if (lsEntry.getFilename().equals(directoryName)) {
                        directoryExist = true;
                    }
                }
            }

            this.getChannelSftp().cd(file.getDirectory());
            files = this.getChannelSftp().ls(file.getDirectory());

            if (files.isEmpty()) {
                this.directoryEmpty = true;
                return;
            }

            for (int i = 0; i < files.size(); i++) {
                ChannelSftp.LsEntry lsEntry = (ChannelSftp.LsEntry) files.get(i);
                if (!lsEntry.getFilename().equals(".") && !lsEntry.getFilename().equals("..") && lsEntry.getFilename().equals(file.getFileName())) {
                    fileExist = true;
                }
            }
        }

        public final ChannelSftp getChannelSftp() {
            return channelSftp;
        }

        public Boolean getDirectoryExist() {
            return directoryExist;
        }

        public Boolean getDirectoryEmpty() {
            return directoryEmpty;
        }

        public Boolean getFileExist() {
            return fileExist;
        }

        public List getFiles() {
            return files;
        }

        public final void disconnect() {
            channel.disconnect();
            channelSftp.disconnect();
            session.disconnect();
        }
    }

    @Override
    public void transfer(FtpPropertie propertie1, FtpFile file1, FtpPropertie propertie2, FtpFile file2, Boolean forceTransfer) throws FtpConnectionException, FileAlreadyExistsException, FileDoesNotExistsException {
        SftpConnection sftpConnection1 = null;
        SftpConnection sftpConnection2 = null;

        try {
            sftpConnection1 = new SftpConnection(propertie1, file1);
            sftpConnection2 = new SftpConnection(propertie2, file2);

            if (!sftpConnection1.getDirectoryExist()) {
                throw new FileDoesNotExistsException("Directory does not exist..");
            }

            if (!sftpConnection1.getFileExist()) {
                throw new FileDoesNotExistsException("File does not exist..");
            }

            if (!sftpConnection2.getDirectoryExist()) {
                throw new FileDoesNotExistsException("Directory does not exist..");
            }

            if (sftpConnection2.getFileExist()) {
                if (forceTransfer) {
                    InputStream is = sftpConnection1.getChannelSftp().get(file1.getFileName());
                    sftpConnection2.getChannelSftp().put(is, file2.getFileName());
                } else {
                    throw new FileAlreadyExistsException("File is already exist..");
                }
            } else {
                InputStream is = sftpConnection1.getChannelSftp().get(file1.getFileName());
                sftpConnection2.getChannelSftp().put(is, file2.getFileName());
            }

        } catch (FileDoesNotExistsException e) {
            Logger.getLogger(SftpConnector.class).error(e, e);
            throw new FileDoesNotExistsException(e);
        } catch (FileAlreadyExistsException e) {
            Logger.getLogger(SftpConnector.class).error(e, e);
            throw new FileAlreadyExistsException(e);
        } catch (Exception e) {
            Logger.getLogger(SftpConnector.class).error(e, e);
            throw new FtpConnectionException(e);
        } finally {
            try {
                sftpConnection1.disconnect();
                sftpConnection2.disconnect();
            } catch (Exception ex) {
                throw new FtpConnectionException(ex);
            }
        }
    }

    @Override
    public void transfer(FtpPropertie propertie, FtpFile file1, File file2, Boolean forceTransfer) throws FtpConnectionException, FileAlreadyExistsException, FileDoesNotExistsException {

        SftpConnection sftpConnection = null;
        try {

            sftpConnection = new SftpConnection(propertie, file1);

            if (!sftpConnection.getDirectoryExist()) {
                throw new FileDoesNotExistsException("Directory does not exist..");
            }

            if (!sftpConnection.getFileExist()) {
                throw new FileDoesNotExistsException("File does not exist..");
            }

            if (file2.exists()) {
                if (forceTransfer) {
                    InputStream is = sftpConnection.getChannelSftp().get(file1.getFileName());
                    OutputStream os = new FileOutputStream(file2);
                    StreamUtils.transfer(is, os, bufferSize);
                } else {
                    throw new FileAlreadyExistsException("file is already exist..");
                }
            } else {
                InputStream is = sftpConnection.getChannelSftp().get(file1.getFileName());
                OutputStream os = new FileOutputStream(file2);
                StreamUtils.transfer(is, os, bufferSize);
            }
        } catch (FileDoesNotExistsException e) {
            Logger.getLogger(SftpConnector.class).error(e, e);
            throw new FileDoesNotExistsException(e);
        } catch (FileAlreadyExistsException e) {
            Logger.getLogger(SftpConnector.class).error(e, e);
            throw new FileAlreadyExistsException(e);
        } catch (IOException e) {
            Logger.getLogger(SftpConnector.class).error(e, e);
            throw new FtpConnectionException(e);
        } catch (Exception e) {
            Logger.getLogger(SftpConnector.class).error(e, e);
            throw new FtpConnectionException(e);
        } finally {
            try {
                sftpConnection.disconnect();
            } catch (Exception ex) {
                throw new FtpConnectionException(ex);
            }
        }
    }

    @Override
    public void transfer(File file1, FtpPropertie propertie, FtpFile file2, Boolean forceTransfer) throws FtpConnectionException, FileAlreadyExistsException, FileDoesNotExistsException {
        SftpConnection sftpConnection = null;

        try {

            sftpConnection = new SftpConnection(propertie, file2);

            if (!sftpConnection.getDirectoryExist()) {
                throw new FileDoesNotExistsException("Directory does not exist..");
            }

            if (!sftpConnection.getFileExist()) {
                throw new FileDoesNotExistsException("File does not exist..");
            }


            if (sftpConnection.getFileExist()) {
                if (forceTransfer) {
                    sftpConnection.getChannelSftp().put(new FileInputStream(file1), file1.getName());
                } else {
                    throw new FileAlreadyExistsException("File is already exist..");
                }
            } else {
                sftpConnection.getChannelSftp().put(new FileInputStream(file1), file1.getName());
            }
        } catch (FileDoesNotExistsException e) {
            Logger.getLogger(SftpConnector.class).error(e, e);
            throw new FileDoesNotExistsException(e);
        } catch (FileAlreadyExistsException e) {
            Logger.getLogger(SftpConnector.class).error(e, e);
            throw new FileAlreadyExistsException(e);
        } catch (IOException e) {
            Logger.getLogger(SftpConnector.class).error(e, e);
            throw new FtpConnectionException(e);
        } catch (Exception e) {
            Logger.getLogger(SftpConnector.class).error(e, e);
            throw new FtpConnectionException(e);
        } finally {
            try {
                sftpConnection.disconnect();
            } catch (Exception ex) {
                throw new FtpConnectionException(ex);
            }
        }
    }

    @Override
    public void removeFile(FtpPropertie propertie, FtpFile file) throws FtpConnectionException, FileDoesNotExistsException {

        SftpConnection sftpConnection = null;
        try {
            sftpConnection = new SftpConnection(propertie, file);

            if (!sftpConnection.getDirectoryExist()) {
                throw new FileDoesNotExistsException("Directory does not exist..");
            }

            if (sftpConnection.getFileExist()) {
                sftpConnection.getChannelSftp().rm(file.getFileName());
            } else {
                throw new FileDoesNotExistsException("File does not exist..");
            }
        } catch (FileDoesNotExistsException e) {
            Logger.getLogger(SftpConnector.class).error(e, e);
            throw new FileDoesNotExistsException(e);
        } catch (Exception e) {
            Logger.getLogger(SftpConnector.class).error(e, e);
            throw new FtpConnectionException(e);
        } finally {
            try {
                sftpConnection.disconnect();
            } catch (Exception ex) {
                throw new FtpConnectionException(ex);
            }
        }
    }

    @Override
    public void removeDirectory(FtpPropertie propertie, FtpFile file, Boolean deleteCascade) throws FtpConnectionException, FileDoesNotExistsException {

        SftpConnection sftpConnection = null;
        try {

            sftpConnection = new SftpConnection(propertie, file);

            if (!sftpConnection.getDirectoryExist()) {
                throw new FileDoesNotExistsException("Directory does not exist..");
            }

            if (!sftpConnection.getDirectoryEmpty()) {
                List files = sftpConnection.getFiles();
                if (deleteCascade) {
                    Integer filesSize = files.size();
                    for (int i = 0; i < filesSize; i++) {
                        ChannelSftp.LsEntry lsEntry = (ChannelSftp.LsEntry) files.get(i);
                        if (!lsEntry.getFilename().equals(".") && !lsEntry.getFilename().equals("..")) {
                            sftpConnection.getChannelSftp().rm(lsEntry.getFilename());
                        }
                    }
                    sftpConnection.getChannelSftp().rmdir(file.getDirectory());
                } else {
                    return;
                }
            } else {
                sftpConnection.getChannelSftp().rmdir(file.getDirectory());
            }
        } catch (FileDoesNotExistsException e) {
            Logger.getLogger(SftpConnector.class).error(e, e);
            throw new FileDoesNotExistsException(e);
        } catch (Exception e) {
            Logger.getLogger(SftpConnector.class).error(e, e);
            throw new FtpConnectionException(e);
        } finally {
            try {
                sftpConnection.disconnect();
            } catch (Exception ex) {
                throw new FtpConnectionException(ex);
            }
        }
    }

    @Override
    public void makeDirectory(FtpPropertie propertie, FtpFile file) throws FtpConnectionException, FileAlreadyExistsException {

        SftpConnection sftpConnection = null;
        try {

            sftpConnection = new SftpConnection(propertie, file);

            if (!sftpConnection.getDirectoryExist()) {
                sftpConnection.getChannelSftp().mkdir(file.getDirectory());
            } else {
                throw new FileAlreadyExistsException("Directory does already exist..");
            }

        } catch (FileAlreadyExistsException e) {
            Logger.getLogger(SftpConnector.class).error(e, e);
            throw new FileAlreadyExistsException(e);
        } catch (Exception e) {
            Logger.getLogger(SftpConnector.class).error(e, e);
            throw new FtpConnectionException(e);
        } finally {
            try {
                sftpConnection.disconnect();
            } catch (Exception ex) {
                throw new FtpConnectionException(ex);
            }
        }
    }

    private String getParentDirectory(String directoryPath) {
        String path = directoryPath;
        String directoryName = path.substring(1, path.length() - 1);
        String pth = directoryName.substring(directoryName.lastIndexOf("/") + 1);
        int p = directoryName.length() - pth.length();
        String parentPath = path.substring(0, p + 1);
        return parentPath;
    }

    private String getDirectoryName(String directoryPath) {
        String path = directoryPath;
        String directoryName = path.substring(1, path.length() - 1);
        String pth = directoryName.substring(directoryName.lastIndexOf("/") + 1);
        return pth;
    }
}
