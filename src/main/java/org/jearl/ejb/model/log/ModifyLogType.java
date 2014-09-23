/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ejb.model.log;

/**
 *
 * @author bamasyali
 */
public enum ModifyLogType {

    insert(1), update(2), delete(3), deletePhysical(4);
    private final Integer value;

    private ModifyLogType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
