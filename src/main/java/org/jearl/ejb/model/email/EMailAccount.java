/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ejb.model.email;

/**
 *
 * @author bamasyali
 */
public interface EMailAccount<COMPANY> {

    Integer getEacId();

    void setEacId(Integer id);

    String getEacName();

    void setEacName(String name);

    String getEacHost();

    void setEacHost(String host);

    String getEacPort();

    void setEacPort(String port);

    Boolean getEacSsl();

    void setEacSsl(Boolean ssl);

    Boolean getEacAuth();

    void setEacAuth(Boolean auth);

    String getEacUsername();

    void setEacUsername(String username);

    String getEacPassword();

    void setEacPassword(String password);

    COMPANY getEacCompany();

    void setEacCompany(COMPANY companyId);
}
