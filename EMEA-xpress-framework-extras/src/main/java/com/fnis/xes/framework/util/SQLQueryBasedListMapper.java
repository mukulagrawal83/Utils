package com.fnis.xes.framework.util;

import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class SQLQueryBasedListMapper implements ListMapper {

    private DataSource dataSource;
    private String sqlMappingQuery;
    private boolean returnFirstMatch = false;
    private static final Logger log = Logger.getLogger(SQLQueryBasedListMapper.class);

    public List<String> map(List<String> values) throws MappingException {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        LinkedList<String> result = new LinkedList<String>();

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sqlMappingQuery);

            int i = 1;
            for (String s : values) {
                ps.setObject(i, s);
                i++;
            }
            rs = ps.executeQuery();

            if (rs.next()) {
                for (i = 1; i < (rs.getMetaData().getColumnCount() + 1); i++) {
                    result.add(rs.getString(i));
                }
                if (!returnFirstMatch && rs.next()) {
                    throw new NonUniqueMappingException("Multiple possible values found as mappable to input values " + values + " - cannot map uniquely");
                }
            }
            return result;
        } catch (SQLException e) {
            throw new MappingException(e);
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

    public boolean isReturnFirstMatch() {
        return returnFirstMatch;
    }

    public void setReturnFirstMatch(boolean returnFirstMatch) {
        this.returnFirstMatch = returnFirstMatch;
    }
}
