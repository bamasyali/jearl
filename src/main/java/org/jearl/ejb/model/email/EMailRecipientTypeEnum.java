/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ejb.model.email;

/**
 *
 * @author bamasyali
 */
public enum EMailRecipientTypeEnum {

    TO(1), CC(2), BCC(3);
    private final Integer value;

    private EMailRecipientTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
