/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ldap.service;

import java.util.List;
import javax.naming.NamingException;
import javax.naming.directory.SearchResult;

/**
 *
 * @author bamasyali
 */
public interface LDapService {

    Boolean auteticate(String username, String password);

    List<SearchResult> search(String query) throws NamingException;
}
