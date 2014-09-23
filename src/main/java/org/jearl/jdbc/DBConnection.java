/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.jdbc;

import java.sql.*;
import org.jearl.jdbc.exception.DBException;

/**
 * @author Mustafa Burak Amasyalı <bamasyali@hotmail.com.tr>
 * @version 2010.0727 (E.g. ISO 8601 YYYY.MMDD)
 * @since 1.6 (The Java version used)
 */
public abstract class DBConnection {

    /**
     * Returns a database connection
     *
     * @exception Exception
     * @return active database connection
     */
    public abstract Connection getConnection() throws DBException;

    /**
     * Returns a database connection
     *
     * @exception Exception
     * @return active database connection
     */
    public abstract void releaseCon(Connection connection);

    /**
     * Executes a query on database
     *
     * Use {@link #getCon()} to get a connection.
     *
     * @param sqlQuery The query which will be executed
     * @exception Exception
     *
     */
    public Boolean executeQuery(final String sqlQuery) throws DBException {
        Connection con = null;
        try {
            con = getConnection();
            PreparedStatement pst;
            if (con != null && !con.isClosed()) {
                pst = con.prepareStatement(sqlQuery);
            } else {
                throw new DBException("Connection is closed");
            }
            return pst.execute();
        } catch (DBException ex) {
            throw new DBException(ex);
        } catch (SQLException ex) {
            throw new DBException(ex);
        } catch (Exception ex) {
            throw new DBException(ex);
        } finally {
            releaseCon(con);
        }
    }

    /**
     * Executes a query on database.
     *
     * Use {@link #getCon()} to get a connection.
     *
     * @param sqlQuery The query which will be executed
     * @param args Objects needed in query
     * @exception Exception
     */
    public Boolean executeQuery(final String sqlQuery, Object... args) throws DBException {
        Connection con = null;
        try {
            con = getConnection();
            PreparedStatement pst;
            if (con != null && !con.isClosed()) {
                pst = con.prepareStatement(sqlQuery);
            } else {
                throw new DBException("Connection is closed");
            }
            for (int i = 0; i < args.length; i++) {
                pst.setObject(i + 1, args[i]);
            }
            return pst.execute();
        } catch (DBException ex) {
            throw new DBException(ex);
        } catch (SQLException ex) {
            throw new DBException(ex);
        } catch (Exception ex) {
            throw new DBException(ex);
        } finally {
            releaseCon(con);
        }
    }

    /**
     * Executes a query on database
     *
     * Use {@link #getCon()} to get a connection.
     *
     * @param sqlQuery The query which will be executed
     * @exception Exception
     *
     */
    public int executeUpdate(final String sqlQuery) throws DBException {
        Connection con = null;
        try {
            con = getConnection();
            PreparedStatement pst;
            if (con != null && !con.isClosed()) {
                pst = con.prepareStatement(sqlQuery);
            } else {
                throw new DBException("Connection is closed");
            }
            return pst.executeUpdate();
        } catch (DBException ex) {
            throw new DBException(ex);
        } catch (SQLException ex) {
            throw new DBException(ex);
        } catch (Exception ex) {
            throw new DBException(ex);
        } finally {
            releaseCon(con);
        }
    }

    /**
     * Executes a query on database.
     *
     * Use {@link #getCon()} to get a connection.
     *
     * @param sqlQuery The query which will be executed
     * @param args Objects needed in query
     * @exception Exception
     */
    public int executeUpdate(final String sqlQuery, Object... args) throws DBException {
        Connection con = null;
        try {
            con = getConnection();
            PreparedStatement pst;
            if (con != null && !con.isClosed()) {
                pst = con.prepareStatement(sqlQuery);
            } else {
                throw new DBException("Connection is closed");
            }
            for (int i = 0; i < args.length; i++) {
                pst.setObject(i + 1, args[i]);
            }
            return pst.executeUpdate();
        } catch (DBException ex) {
            throw new DBException(ex);
        } catch (SQLException ex) {
            throw new DBException(ex);
        } catch (Exception ex) {
            throw new DBException(ex);
        } finally {
            releaseCon(con);
        }
    }

    /**
     * Executes a procedure on database.
     *
     * Use {@link #getCon()} to get a connection.
     *
     * @param procedure The procedure which will be executed. Using like "{call
     * procedure()"
     * @exception Exception
     */
    public Boolean executeProcedure(String procedure) throws DBException {
        Connection con = null;
        try {
            con = getConnection();
            PreparedStatement pst = con.prepareCall(procedure);
            return pst.execute();
        } catch (DBException ex) {
            throw new DBException(ex);
        } catch (SQLException ex) {
            throw new DBException(ex);
        } catch (Exception ex) {
            throw new DBException(ex);
        } finally {
            releaseCon(con);
        }
    }

    /**
     * Executes a procedure on database.
     *
     * Use {@link #getCon()} to get a connection.
     *
     * @param procedure Procedure which will be executed. Using like "{call
     * procedure(?,?)"
     * @param args objects needed in query
     * @exception Exception
     */
    public Boolean executeProcedure(String procedure, Object... args) throws DBException {
        Connection con = null;
        try {
            con = getConnection();
            CallableStatement pst = con.prepareCall(procedure);
            for (int i = 0; i < args.length; i++) {
                pst.setObject(i + 1, args[i]);
            }
            return pst.execute();
        } catch (DBException ex) {
            throw new DBException(ex);
        } catch (SQLException ex) {
            throw new DBException(ex);
        } catch (Exception ex) {
            throw new DBException(ex);
        } finally {
            releaseCon(con);
        }
    }

    /**
     * Return result from database.
     *
     * Use {@link #getCon()} to get a connection.
     *
     * @param sqlQuery Query which will be retuned
     * @return ResultSet
     * @exception Exception
     */
    public ResultSet returnResult(final String sqlQuery) throws DBException {

        Connection con = null;
        try {
            con = getConnection();
            PreparedStatement pst;
            if (con != null && !con.isClosed()) {
                pst = con.prepareStatement(sqlQuery);
            } else {
                throw new DBException("Connection is closed");
            }
            return pst.executeQuery();
        } catch (DBException ex) {
            throw new DBException(ex);
        } catch (SQLException ex) {
            throw new DBException(ex);
        } catch (Exception ex) {
            throw new DBException(ex);
        } finally {
            releaseCon(con);
        }
    }

    /**
     * Return result from database.
     *
     * Use {@link #getCon()} to get a connection.
     *
     * @param sqlQuery Query which will be retuned
     * @param args Objects needed in sqlQuery
     * @return ResultSet
     * @exception Exception
     */
    public ResultSet returnResult(final String sqlQuery, Object... args) throws DBException {

        Connection con = null;
        try {
            con = getConnection();
            PreparedStatement pst;
            if (con != null && !con.isClosed()) {
                pst = con.prepareStatement(sqlQuery);
            } else {
                throw new DBException("Connection is closed");
            }
            for (int i = 0; i < args.length; i++) {
                pst.setObject(i + 1, args[i]);
            }
            return pst.executeQuery();
        } catch (DBException ex) {
            throw new DBException(ex);
        } catch (SQLException ex) {
            throw new DBException(ex);
        } catch (Exception ex) {
            throw new DBException(ex);
        } finally {
            releaseCon(con);
        }
    }

    /**
     * Return result from database.
     *
     * Use {@link #getCon()} to get a connection.
     *
     * @param sqlQuery Query which will be retuned
     * @return ResultSet
     * @exception Exception
     */
    public ResultSet returnProcedureResult(final String sqlQuery) throws DBException {

        Connection con = null;
        try {
            con = getConnection();
            CallableStatement pst;
            if (con != null && !con.isClosed()) {
                pst = con.prepareCall(sqlQuery);
            } else {
                throw new DBException("Connection is closed");
            }
            return pst.executeQuery();
        } catch (DBException ex) {
            throw new DBException(ex);
        } catch (SQLException ex) {
            throw new DBException(ex);
        } catch (Exception ex) {
            throw new DBException(ex);
        } finally {
            releaseCon(con);
        }
    }

    /**
     * Return result from database.
     *
     * Use {@link #getCon()} to get a connection.
     *
     * @param sqlQuery Query which will be retuned
     * @param args Objects needed in sqlQuery
     * @return ResultSet
     * @exception Exception
     */
    public ResultSet returnProcedureResult(final String sqlQuery, Object... args) throws DBException {

        Connection con = null;
        try {
            con = getConnection();
            CallableStatement pst;
            if (con != null && !con.isClosed()) {
                pst = con.prepareCall(sqlQuery);
            } else {
                throw new DBException("Connection is closed");
            }
            for (int i = 0; i < args.length; i++) {
                pst.setObject(i + 1, args[i]);
            }
            return pst.executeQuery();
        } catch (DBException ex) {
            throw new DBException(ex);
        } catch (SQLException ex) {
            throw new DBException(ex);
        } catch (Exception ex) {
            throw new DBException(ex);
        } finally {
            releaseCon(con);
        }
    }
}
