/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.log;

import org.jearl.logback.ErrorCategory;
import org.apache.log4j.Level;

/**
 *
 * @author bamasyali
 */
public class JearlLoggerImpl implements JearlLogger {

    private final org.apache.log4j.Logger logger;

    public JearlLoggerImpl(Class clazz) {
        
        logger = Logger.getLogger(clazz);
    }

    @Override
    public void changeLevelTreshold(Level l) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void fillAllMDC(ErrorCategory category) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void fillAllMDC(Integer userid, ErrorCategory category) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void fillAllMDC(Integer userid, ErrorCategory category, Throwable t) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void audit(Object msg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void trace(Object msg) {
        this.logger.trace(msg);
    }

    @Override
    public void trace(Object msg, Throwable t) {
        this.logger.trace(msg, t);
    }

    @Override
    public boolean isTraceEnabled() {
        return this.logger.isTraceEnabled();
    }

    @Override
    public void debug(Object msg) {
        this.logger.debug(msg);
    }

    @Override
    public void debug(Object msg, Throwable t) {
        this.logger.debug(msg, t);
    }

    @Override
    public void debug(Integer userid, ErrorCategory category, Object msg, Throwable t) {
        this.logger.trace(msg, t);
    }

    @Override
    public boolean isDebugEnabled() {
        return this.logger.isDebugEnabled();
    }

    @Override
    public void info(Object msg) {
        this.logger.info(msg);
    }

    @Override
    public void info(Object msg, Throwable t) {
        this.logger.info(msg, t);
    }

    @Override
    public boolean isInfoEnabled() {
        return this.logger.isInfoEnabled();
    }

    @Override
    public void warn(Object msg) {
        this.logger.warn(msg);
    }

    @Override
    public void warn(Object msg, Throwable t) {
        this.logger.warn(msg, t);
    }

    @Override
    public void warn(Integer userid, ErrorCategory category, Object msg, Throwable t) {
        this.logger.warn(msg, t);
    }

    @Override
    public void error(Object msg, ErrorCategory category) {
        this.logger.error(msg);
    }

    @Override
    public void error(Object msg, ErrorCategory category, Throwable t) {
        this.logger.error(msg, t);
    }

    @Override
    public void error(Integer userid, ErrorCategory category, Throwable t) {
        this.logger.error(category, t);
    }

    @Override
    public void error(Integer userid, ErrorCategory category, Object msg) {
        this.logger.error(msg);
    }

    @Override
    public void error(Integer userid, ErrorCategory category, Object msg, Throwable t) {
        this.logger.error(msg, t);
    }

    @Override
    public void fatal(Object msg) {
        this.logger.fatal(msg);
    }

    @Override
    public void fatal(ErrorCategory category, Object msg, Throwable t) {
        this.logger.fatal(msg, t);
    }

    @Override
    public void fatal(Object msg, Throwable t) {
        this.logger.fatal(msg, t);
    }

    @Override
    public void fatal(Integer userid, ErrorCategory category, Object msg, Throwable t) {
        this.logger.fatal(msg, t);
    }

    @Override
    public void logNestedException(Level level, Object msg, Throwable t) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
