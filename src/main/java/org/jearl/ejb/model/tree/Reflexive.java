/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ejb.model.tree;

import java.util.List;

/**
 *
 * @author can
 */
public interface Reflexive<T> {

    T getParent();

    void setParent(T entity);

    List<T> getChildren();

    void setChildren(List<T> entity);
}
