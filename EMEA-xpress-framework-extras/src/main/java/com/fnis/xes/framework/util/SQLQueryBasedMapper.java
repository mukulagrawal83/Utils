package com.fnis.xes.framework.util;

import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLQueryBasedMapper implements Mapper {

    private DataSource dataSource;
    private String sqlMappingQuery;
    private static final Logger log = Logger.getLogger(SQLQueryBasedMapper.class);

    public String map(String value) throws MappingException {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result = null;

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sqlMappingQuery);

            ps.setObject(1, value);

            rs = ps.executeQuery();

            if (rs.next()) {
                result = rs.getString(1);
                if (rs.next())
                    throw new NonUniqueMappingException("Multiple possible values found as mappable to input value " + value + " - cannot map uniquely");
            }
            return result;
        } catch (SQLException e) {
            throw new MappingException("Exception caught ", e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    log.warn("Failed to close rs ", e);
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    log.warn("Failed to close ps ", e);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    log.warn("Failed to close conn ", e);
                }
            }
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getSqlMappingQuery() {
        return sqlMappingQuery;
    }

    public void setSqlMappingQuery(String sqlMappingQuery) {
        this.sqlMappingQuery = sqlMappingQuery;
    }
}
