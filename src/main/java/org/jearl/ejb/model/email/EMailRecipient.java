/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ejb.model.email;

/**
 *
 * @author bamasyali
 */
public interface EMailRecipient {

    Integer getRcpId();

    void setRcpId(Integer id);

    String getRcpAdress();

    void setRcpAdress(String adress);

    EMail getRcpMail();

    void setRcpMail(EMail mail);

    EMailRecipientType getRcpRecipientType();

    void setRcpRecipientType(EMailRecipientType eMailRecipientType);
}
