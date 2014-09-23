/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ejb.model.email;

import java.util.Date;
import java.util.List;

/**
 *
 * @author bamasyali
 */
public interface EMail<USER> {

    Integer getMailId();

    void setMailId(Integer id);

    String getMailFrom();

    void setMailFrom(String from);

    String getMailSenderip();

    void setMailSenderip(String senderip);

    Date getMailSenttime();

    void setMailSenttime(Date senttime);

    String getMailTopic();

    void setMailTopic(String tpic);

    String getMailContent();

    void setMailContent(String content);

    USER getMailUser();

    void setMailUser(USER usrUser);

    EMailAccount getMailAccount();

    void setMailAccount(EMailAccount eMailAccount);

    List<EMailRecipient> getMailRecipientList();

    void setMailRecipientList(List<EMailRecipient> eMailRecipients);
}
