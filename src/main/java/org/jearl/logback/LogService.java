/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.logback;

import ch.qos.logback.classic.Level;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.Marker;

/**
 *
 * @author can
 */
public class LogService implements Log {

    private final org.slf4j.Logger logger;
    // private static final Integer STACKTRACE_LIMIT = 10;

    public LogService(Class clazz) {
        logger = LoggerFactory.getLogger(clazz);
    }

    @Override
    public void changeLevelTreshold(Level l) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static void putMDC(CustomLogField logField, String value) {
        if (value != null) {
            MDC.put(logField.getValue(), (String) value);
        }
    }

    private void putArgument(CustomLogField logField, Object arg) {
        String putValue = null;
        if (arg != null) {
            if (arg instanceof ErrorCategory) {
                putValue = ((ErrorCategory) arg).getValue();
            } else {
                putValue = arg.toString();
            }
            putMDC(logField, putValue);
        }
    }

    private String convert(Integer value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    @Override
    public void trace(String msg) {
        if (isTraceEnabled()) {
            logger.trace((String) msg);
            MDC.clear();
        }
    }

    @Override
    public void trace(String msg, Throwable t) {
        if (isTraceEnabled()) {
            logger.trace((String) msg, t);
            MDC.clear();
        }
    }

    @Override
    public boolean isTraceEnabled() {
        return this.logger.isTraceEnabled();
    }

    @Override
    public void info(String msg) {
        if (isInfoEnabled()) {
            this.logger.info(msg);
        }
    }

    @Override
    public void info(String msg, Throwable t) {
        if (isInfoEnabled()) {
            this.logger.info(msg, t);
        }
    }

    @Override
    public void debug(String msg) {
        if (isDebugEnabled()) {
            this.logger.debug((String) msg);
        }
    }

    @Override
    public void debug(String msg, Throwable t) {
        if (isDebugEnabled()) {
            this.logger.debug((String) msg, t);
        }
    }

    @Override
    public void debug(Integer userid, ErrorCategory category, String msg, Throwable t) {
        if (isDebugEnabled()) {
            putArgument(CustomLogField.LOG_USER, convert(userid));
            putArgument(CustomLogField.ERROR_CATEGORY, category);
            this.logger.debug(msg, t);
            MDC.clear();
        }
    }

    @Override
    public void debug(String appContainer, Integer userid, ErrorCategory category, String msg, Throwable t) {
        if (isDebugEnabled()) {
            putArgument(CustomLogField.LOG_USER, convert(userid));
            putArgument(CustomLogField.ERROR_CATEGORY, category);
            this.logger.debug((String) msg, t);
            MDC.clear();
        }
    }

    @Override
    public boolean isDebugEnabled() {
        return this.logger.isDebugEnabled();
    }

    @Override
    public void warn(String appContainer, String msg) {
        if (isWarnEnabled()) {
            putArgument(CustomLogField.LOG_CONTAINER, appContainer);
            this.logger.warn(msg);
            MDC.clear();
        }
    }

    @Override
    public void warn(String appContainer, String msg, Throwable t) {
        if (isWarnEnabled()) {
            putArgument(CustomLogField.LOG_CONTAINER, appContainer);
            this.logger.warn(msg, t);
            MDC.clear();
        }
    }

    @Override
    public void warn(String appContainer, Integer userid, ErrorCategory category, String msg, Throwable t) {
        if (isWarnEnabled()) {
            putArgument(CustomLogField.LOG_CONTAINER, appContainer);
            putArgument(CustomLogField.LOG_USER, convert(userid));
            putArgument(CustomLogField.ERROR_CATEGORY, category);
            this.logger.warn(msg, t);
            MDC.clear();
        }
    }

    @Override
    public void error(String appContainer, String msg) {
        if (isErrorEnabled()) {
            logger.error((String) msg);
            MDC.clear();
        }

    }

    @Override
    public void error(String appContainer, String msg, ErrorCategory category) {
        if (isErrorEnabled()) {
            putArgument(CustomLogField.LOG_CONTAINER, appContainer);
            putArgument(CustomLogField.ERROR_CATEGORY, category);
            logger.error((String) msg);
            MDC.clear();
        }
    }

    @Override
    public void error(String appContainer, Integer userid, String msg, ErrorCategory category) {
        if (isErrorEnabled()) {
            putArgument(CustomLogField.LOG_USER, userid);
            putArgument(CustomLogField.ERROR_CATEGORY, category);
            putArgument(CustomLogField.LOG_CONTAINER, appContainer);
            logger.error((String) msg);
            MDC.clear();
        }
    }

    @Override
    public void error(String appContainer, Integer userid, String msg) {
        if (isErrorEnabled()) {
            putArgument(CustomLogField.LOG_USER, convert(userid));
            putArgument(CustomLogField.LOG_CONTAINER, appContainer);
            logger.error((String) msg);
            MDC.clear();
        }
    }

    @Override
    public void error(String appContainer, String msg, ErrorCategory category, Throwable t) {
        if (isErrorEnabled()) {
            putArgument(CustomLogField.ERROR_CATEGORY, category);
            putArgument(CustomLogField.LOG_CONTAINER, appContainer);
            logger.error((String) msg, t);
            MDC.clear();
        }
    }

    @Override
    public void error(String appContainer, Integer userid, ErrorCategory category, String msg) {
        if (isErrorEnabled()) {
            putArgument(CustomLogField.LOG_USER, convert(userid));
            putArgument(CustomLogField.ERROR_CATEGORY, category);
            putArgument(CustomLogField.LOG_CONTAINER, appContainer);
            this.logger.error(msg);
            MDC.clear();
        }
    }

    @Override
    public void error(String appContainer, Integer userid, ErrorCategory category, String msg, Throwable t) {
        if (isErrorEnabled()) {
            putArgument(CustomLogField.LOG_USER, convert(userid));
            putArgument(CustomLogField.ERROR_CATEGORY, category);
            putArgument(CustomLogField.LOG_CONTAINER, appContainer);
            this.logger.error(msg, t);
            MDC.clear();
        }
    }

    @Override
    public void logNestedException(Level level, String msg, Throwable t) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public StringBuilder getPrintedStackTrace(Throwable t, int limit) {
        StringBuilder builder = new StringBuilder();
        if (t != null) {
            StackTraceElement[] traceElements = t.getStackTrace();
            if (traceElements != null) {
                int iterationCount = traceElements.length;
                if (iterationCount > limit) {
                    iterationCount = limit;
                }
                for (int i = 0; i < iterationCount; i++) {
                    builder.append("at ");
                    builder.append("---");
                    builder.append(traceElements[i].getClassName());
                    builder.append(".");
                    builder.append(traceElements[i].getMethodName());
                    builder.append(".");
                    builder.append("(");
                    builder.append(traceElements[i].getFileName());
                    builder.append(traceElements[i].getLineNumber());
                    builder.append(")");
                    builder.append("\n");
                }
            }
        }
        return builder;
    }

    public String getReadyErrorCauseMessage(Throwable t, int limit) {
        if (t != null) {
            String msg = "";
            if (t.getMessage() != null) {
                msg = t.toString();
            }
            msg += "\n";
            msg += getPrintedStackTrace(t, limit);
            return msg;
        } else {
            return null;
        }
    }

    @Override
    public String getName() {
        return this.logger.getName();
    }

    @Override
    public void trace(String string, Object o) {
        if (isTraceEnabled()) {
            this.logger.trace(string, o);
        }
    }

    @Override
    public void trace(String string, Object o, Object o1) {
        if (isTraceEnabled()) {
            this.logger.trace(string, o, o1);
        }
    }

    @Override
    public void trace(String string, Object[] os) {
        if (isTraceEnabled()) {
            this.logger.trace(string, os);
        }
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return this.logger.isTraceEnabled(marker);
    }

    @Override
    public void trace(Marker marker, String string) {
        if (isTraceEnabled(marker)) {
            this.logger.trace(marker, string);
        }
    }

    @Override
    public void trace(Marker marker, String string, Object o) {
        if (isTraceEnabled(marker)) {
            this.logger.trace(marker, string, o);
        }
    }

    @Override
    public void trace(Marker marker, String string, Object o, Object o1) {
        if (isTraceEnabled(marker)) {
            this.logger.trace(marker, string, o, o1);
        }
    }

    @Override
    public void trace(Marker marker, String string, Object[] os) {
        if (isTraceEnabled(marker)) {
            this.logger.trace(marker, string, os);
        }
    }

    @Override
    public void trace(Marker marker, String string, Throwable thrwbl) {
        if (isTraceEnabled(marker)) {
            this.logger.trace(marker, string, thrwbl);
        }
    }

    @Override
    public void debug(String string, Object o) {
        if (isDebugEnabled()) {
            this.logger.debug(string, o);
        }
    }

    @Override
    public void debug(String string, Object o, Object o1) {
        if (isDebugEnabled()) {
            this.logger.debug(string, o, o1);
        }
    }

    @Override
    public void debug(String string, Object[] os) {
        if (isDebugEnabled()) {
            this.logger.debug(string, os);
        }
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return this.logger.isDebugEnabled(marker);
    }

    @Override
    public void debug(Marker marker, String string) {
        if (isDebugEnabled(marker)) {
            this.logger.debug(marker, string);
        }
    }

    @Override
    public void debug(Marker marker, String string, Object o) {
        if (isDebugEnabled(marker)) {
            this.logger.debug(marker, string, o);
        }
    }

    @Override
    public void debug(Marker marker, String string, Object o, Object o1) {
        if (isDebugEnabled(marker)) {
            this.logger.debug(marker, string, o, o1);
        }
    }

    @Override
    public void debug(Marker marker, String string, Object[] os) {
        if (isDebugEnabled(marker)) {
            this.logger.debug(marker, string, os);
        }
    }

    @Override
    public void debug(Marker marker, String string, Throwable thrwbl) {
        if (isDebugEnabled(marker)) {
            this.logger.debug(marker, string, thrwbl);
        }
    }

    @Override
    public void info(String string, Object o) {
        if (isInfoEnabled()) {
            this.logger.info(string, o);
        }
    }

    @Override
    public void info(String string, Object o, Object o1) {
        if (isInfoEnabled()) {
            this.logger.info(string, o, o1);
        }
    }

    @Override
    public void info(String string, Object[] os) {
        if (isInfoEnabled()) {
            this.logger.info(string, os);
        }
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return this.logger.isInfoEnabled(marker);
    }

    @Override
    public boolean isInfoEnabled() {
        return this.logger.isInfoEnabled();
    }

    @Override
    public void info(Marker marker, String string) {
        if (isInfoEnabled(marker)) {
            this.logger.info(marker, string);
        }
    }

    @Override
    public void info(Marker marker, String string, Object o) {
        if (isInfoEnabled(marker)) {
            this.logger.info(marker, string, o);
        }
    }

    @Override
    public void info(Marker marker, String string, Object o, Object o1) {
        if (isInfoEnabled(marker)) {
            this.logger.info(marker, string, o, o1);
        }
    }

    @Override
    public void info(Marker marker, String string, Object[] os) {
        if (isInfoEnabled(marker)) {
            this.logger.info(marker, string, os);
        }
    }

    @Override
    public void info(Marker marker, String string, Throwable thrwbl) {
        if (isInfoEnabled(marker)) {
            this.logger.info(marker, string, thrwbl);
        }
    }

    @Override
    public boolean isWarnEnabled() {
        return this.logger.isWarnEnabled();
    }

    @Override
    public void warn(String string) {
        if (isWarnEnabled()) {
            this.logger.warn(string);
        }
    }

    @Override
    public void warn(String string, Object o) {
        if (isWarnEnabled()) {
            this.logger.warn(string, o);
        }
    }

    @Override
    public void warn(String string, Object[] os) {
        if (isWarnEnabled()) {
            this.warn(string, os);
        }
    }

    @Override
    public void warn(String string, Object o, Object o1) {
        if (isWarnEnabled()) {
            this.logger.warn(string, o, o1);
        }
    }

    @Override
    public void warn(String string, Throwable thrwbl) {
        if (isWarnEnabled()) {
            this.logger.warn(string, thrwbl);
        }
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return this.logger.isWarnEnabled();
    }

    @Override
    public void warn(Marker marker, String string) {
        if (isWarnEnabled(marker)) {
            this.logger.warn(marker, string);
        }
    }

    @Override
    public void warn(Marker marker, String string, Object o) {
        if (isWarnEnabled(marker)) {
            this.logger.warn(marker, string, o);
        }
    }

    @Override
    public void warn(Marker marker, String string, Object o, Object o1) {
        if (isWarnEnabled(marker)) {
            this.logger.warn(marker, string, o, o1);
        }
    }

    @Override
    public void warn(Marker marker, String string, Object[] os) {
        if (isWarnEnabled(marker)) {
            this.logger.warn(marker, string, os);
        }
    }

    @Override
    public void warn(Marker marker, String string, Throwable thrwbl) {
        if (isWarnEnabled(marker)) {
            this.logger.warn(marker, string, thrwbl);
        }
    }

    @Override
    public boolean isErrorEnabled() {
        return this.logger.isErrorEnabled();
    }

    @Override
    public void error(String string) {
        if (isErrorEnabled()) {
            this.logger.error(string);
        }
    }

    @Override
    public void error(String string, Object o) {
        if (isErrorEnabled()) {
            this.logger.error(string, o);
        }
    }

    @Override
    public void error(String string, Object o, Object o1) {
        if (isErrorEnabled()) {
            this.logger.error(string, o, o1);
        }
    }

    @Override
    public void error(String string, Object[] os) {
        if (isErrorEnabled()) {
            this.logger.error(string, os);
        }
    }

    @Override
    public void error(String string, Throwable thrwbl) {
        if (isErrorEnabled()) {
            this.logger.error(string, thrwbl);
        }
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return this.logger.isErrorEnabled(marker);
    }

    @Override
    public void error(Marker marker, String string) {
        if (isErrorEnabled(marker)) {
            this.logger.error(marker, string);
        }
    }

    @Override
    public void error(Marker marker, String string, Object o) {
        if (isErrorEnabled(marker)) {
            this.logger.error(marker, string, o);
        }
    }

    @Override
    public void error(Marker marker, String string, Object o, Object o1) {
        if (isErrorEnabled(marker)) {
            this.logger.error(marker, string, o, o1);
        }
    }

    @Override
    public void error(Marker marker, String string, Object[] os) {
        if (isErrorEnabled(marker)) {
            this.logger.error(marker, string, os);
        }
    }

    @Override
    public void error(Marker marker, String string, Throwable thrwbl) {
        if (isErrorEnabled(marker)) {
            this.logger.error(marker, string, thrwbl);
        }
    }
}
