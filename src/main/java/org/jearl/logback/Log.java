/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.logback;

import ch.qos.logback.classic.Level;
import org.slf4j.Logger;

/**
 *
 * @author can
 */
public interface Log extends Logger {

    void changeLevelTreshold(Level l);

    void debug(Integer userid, ErrorCategory category, String msg, Throwable t);

    void debug(String appContainer, Integer userid, ErrorCategory category, String msg, Throwable t);

    void warn(String appContainer, String msg);

    void warn(String appContainer, String msg, Throwable t);

    void warn(String appContainer, Integer userid, ErrorCategory category, String msg, Throwable t);

    void error(String appContainer, String msg);

    void error(String appContainer, Integer userid, String msg);

    void error(String appContainer, String msg, ErrorCategory category);

    void error(String appContainer, Integer userid, String msg, ErrorCategory category);

    void error(String appContainer, String msg, ErrorCategory category, Throwable t);

    void error(String appContainer, Integer userid, ErrorCategory category, String msg);

    void error(String appContainer, Integer userid, ErrorCategory category, String msg, Throwable t);

    void logNestedException(Level level, String msg, Throwable t);
}
