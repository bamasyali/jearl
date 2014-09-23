/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ejb.model.status;

import java.util.Date;

/**
 *
 * @author nodeser
 */
public interface StatusLog< ENTITY extends EntityWithStatus, STATUS extends Status, USER> {

    Integer getLogId();

    void setLogId(Integer logId);

    Date getLogStatusFromTime();

    void setLogStatusFromTime(Date logStatusFromTime);

    Date getLogStatusToTime();

    void setLogStatusToTime(Date logStatusToTime);

    USER getUser();

    void setUser(USER user);

    STATUS getLogStatusFrom();

    void setLogStatusFrom(STATUS status);

    STATUS getLogStatusTo();

    void setLogStatusTo(STATUS status);

    ENTITY getEntity();

    void setEntity(ENTITY entity);
}
