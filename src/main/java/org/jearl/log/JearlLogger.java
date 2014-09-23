/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.log;

import org.jearl.logback.ErrorCategory;
import org.apache.log4j.Level;

/**
 *
 * @author can
 */
public interface JearlLogger {

    void changeLevelTreshold(Level l);

    void fillAllMDC(ErrorCategory category);

    void fillAllMDC(Integer userid, ErrorCategory category);

    void fillAllMDC(Integer userid, ErrorCategory category, Throwable t);

    void audit(Object msg);

    void trace(Object msg);

    void trace(Object msg, Throwable t);

    boolean isTraceEnabled();

    void debug(Object msg);

    void debug(Object msg, Throwable t);

    void debug(Integer userid, ErrorCategory category, Object msg, Throwable t);

    boolean isDebugEnabled();

    void info(Object msg);

    void info(Object msg, Throwable t);

    boolean isInfoEnabled();

    void warn(Object msg);

    void warn(Object msg, Throwable t);

    void warn(Integer userid, ErrorCategory category, Object msg, Throwable t);

    void error(Object msg, ErrorCategory category);

    void error(Object msg, ErrorCategory category, Throwable t);

    void error(Integer userid, ErrorCategory category, Throwable t);

    void error(Integer userid, ErrorCategory category, Object msg);

    void error(Integer userid, ErrorCategory category, Object msg, Throwable t);

    void fatal(Object msg);

    void fatal(ErrorCategory category, Object msg, Throwable t);

    void fatal(Object msg, Throwable t);

    void fatal(Integer userid, ErrorCategory category, Object msg, Throwable t);

    void logNestedException(Level level, Object msg, Throwable t);
}
