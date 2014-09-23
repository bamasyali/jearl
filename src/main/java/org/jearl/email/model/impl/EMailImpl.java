/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.email.model.impl;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.jearl.email.model.EMail;

/**
 *
 * @author bamasyali
 */
public class EMailImpl implements EMail, Serializable {

    private List<String> to;
    private List<String> cc;
    private List<String> bcc;
    private List<File> files;
    private String topic;
    private String content;
    private String from;
    private String replyTo;

    public EMailImpl() {
        this.to = new ArrayList<String>();
        this.cc = new ArrayList<String>();
        this.bcc = new ArrayList<String>();
        this.files = new ArrayList<File>();
    }

    @Override
    public void addEMailTo(String eMail) {
        to.add(eMail);
    }

    @Override
    public void addEMailCc(String eMail) {
        cc.add(eMail);
    }

    @Override
    public void addEMailBcc(String eMail) {
        bcc.add(eMail);
    }

    @Override
    public void addFile(File file) {
        files.add(file);
    }

    @Override
    public void removeEMailTo(String eMail) {
        to.remove(eMail);
    }

    @Override
    public void removeEMailCc(String eMail) {
        cc.remove(eMail);
    }

    @Override
    public void removeEMailBcc(String eMail) {
        bcc.remove(eMail);
    }

    @Override
    public void setEmailContent(String emailContent) {
        this.content = emailContent;
    }

    @Override
    public void setEmailTopic(String emailTopic) {
        this.topic = emailTopic;
    }

    @Override
    public int getSizeOfTo() {
        return to.size();
    }

    @Override
    public int getSizeOfCc() {
        return cc.size();
    }

    @Override
    public int getSizeOfBcc() {
        return bcc.size();
    }

    @Override
    public List<String> getBcc() {
        return bcc;
    }

    @Override
    public List<String> getCc() {
        return cc;
    }

    @Override
    public List<String> getTo() {
        return to;
    }

    @Override
    public List<File> getFileList() {
        return files;
    }

    @Override
    public String getEmailTopic() {
        return topic;
    }

    @Override
    public String getEmailContent() {
        return content;
    }

    @Override
    public String getFrom() {
        return from;
    }

    @Override
    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public String getReplyTo() {
        return this.replyTo;
    }

    @Override
    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }
}
