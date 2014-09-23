/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.email.model;

import org.jearl.ejb.model.email.EMail;
import org.jearl.ejb.model.email.EMailAccount;
import org.jearl.ejb.model.email.EMailRecipient;
import org.jearl.ejb.model.email.EMailRecipientType;
import org.jearl.ejb.model.email.EMailRecipientTypeEnum;
import javax.persistence.EntityManager;

/**
 *
 * @author bamasyali
 */
public interface EMailLogger<USER> {

    EMail<USER> initEmail();

    EMailRecipient initRecipient();

    EMailRecipientType initRecipientType(EMailRecipientTypeEnum eMailRecipientTypeEnum);

    EntityManager getEntityManager();

    String getUserIp();

    USER getUser();

    EMailAccount getEMailAccount();
}
