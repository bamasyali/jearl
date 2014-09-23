/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.email.model;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author bamasyali
 */
public interface EMail extends Serializable {

    void addEMailTo(String eMail);

    void addEMailCc(String eMail);

    void addEMailBcc(String eMail);

    void addFile(File file);

    void removeEMailTo(String eMail);

    void removeEMailCc(String eMail);

    void removeEMailBcc(String eMail);

    String getEmailTopic();

    void setEmailTopic(String emailTopic);

    String getEmailContent();

    void setEmailContent(String emailContent);

    int getSizeOfTo();

    int getSizeOfCc();

    int getSizeOfBcc();

    List<String> getBcc();

    List<String> getCc();

    List<String> getTo();

    List<File> getFileList();

    String getFrom();

    void setFrom(String from);

    String getReplyTo();

    void setReplyTo(String replyTo);
}
