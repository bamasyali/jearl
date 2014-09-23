/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ejb.model;

import java.io.Serializable;

/**
 *
 * @author bamasyali
 */
public interface BaseEntity<PRIMARY> extends Serializable {

    PRIMARY getPrimayKey();

    Integer getPrimayKeyAsInteger();
}
