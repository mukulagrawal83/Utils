package com.fnf.xes.framework.dhm.sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.fnf.xes.framework.dhm.conversion.DateManager;
import com.fnf.xes.framework.dhm.data.format.FormatFactory;
import com.fnf.xes.framework.dhm.data.format.IFormatFactory;
import com.fnf.xes.framework.dhm.data.model.DataElement;
import com.fnf.xes.framework.dhm.data.model.DataObject;
import com.fnf.xes.framework.dhm.data.model.InvocationMetadata;
import com.fnf.xes.framework.dhm.data.model.Value;
import com.fnf.xes.framework.dhm.data.ops.FactoryType;

/**
 * helper class to build the database query from a DataObject
 *
 * @author nambiarsm
 */
public final class QueryBuilder
{

    private static IFormatFactory _formatFactory;
    private static final String CIF_TAXID = "CIF.TAXID";

    static{
        _formatFactory = FormatFactory.getFormatFactory(FactoryType.PROFILE);
    }
    /**
     *
     */
    private QueryBuilder() {
    }

    /**
     * Builds an UPDATE query
     * @param dataObj
     * @return
     * @throws Exception
     */
    public static Query getUpdateQuery(DataObject dataObj) throws Exception{
        InvocationMetadata meta = dataObj.getInvocationMetadata();
        Iterator columns = dataObj.columnIterator();
        if(!columns.hasNext()){
            throw new IllegalArgumentException("No Columns found for update!.");
        }
        Iterator where = dataObj.whereIterator();
        if(!where.hasNext()){
            throw new IllegalArgumentException("No WHERE clause found!.");
        }
        // Building SET clause
        String tableName = dataObj.getName();
        List formattedAllColumn = new ArrayList(); // now contains all columns including SET and WHERE
        StringBuffer sql = new StringBuffer("UPDATE ")
                .append(tableName).append(" SET ");
        while(columns.hasNext()){
            DataElement ele = (DataElement)columns.next();
            Value value = ele.getValue();
            String colName = ele.getName();
            if(value==null){
                formattedAllColumn.add("NULL");
            } //PS-13704
            else if (colName.equals(CIF_TAXID) && value.getValue() instanceof String) {
                String stringValue = (String)value.getValue();
                formattedAllColumn.add(stringValue);
            } else{
                formattedAllColumn.add(_formatFactory.formatValue(value));
            }
            sql.append(colName).append("=? ,");
        }
        sql = sql.deleteCharAt(sql.length()-1); // deleting the last ',' mark

        // Building the WHERE clause
        sql.append(" WHERE ");
        while(where.hasNext()){
            DataElement ele = (DataElement)where.next();
            Value value = ele.getValue();
            String name = ele.getName();
            formattedAllColumn.add(_formatFactory.formatValue(value));
            sql.append(name).append("=? AND "); // All WHERE conditons are AND
        }
        sql = sql.delete(sql.length()-4, sql.length()); // deleting the last 'AND'
        Query query = new Query(sql.toString(),formattedAllColumn);
        return query;
    }

    /**
     * Builds an INSERT query
     * @param dataObj
     * @return
     * @throws Exception
     */
    public static Query getInsertQuery(DataObject dataObj) throws Exception{
        InvocationMetadata meta = dataObj.getInvocationMetadata();
        Iterator columns = dataObj.columnIterator();
        if(!columns.hasNext()){
            throw new IllegalArgumentException("No Columns found for INSERT!.");
        }

        // Building INSERT values clause
        String tableName = dataObj.getName();
        List formattedAllColumn = new ArrayList(); // now contains all columns including SET and WHERE
        StringBuffer sql = new StringBuffer("INSERT INTO ")
                .append(tableName).append(" (");
        StringBuffer valuesHolder = new StringBuffer(" VALUES(");
        while(columns.hasNext()){
            DataElement ele = (DataElement)columns.next();
            Value value = ele.getValue();
            String colName = ele.getName();
            if(value==null){
                formattedAllColumn.add("NULL");
            }else{
                if(!value.equals(Value.newSequence())){
                    formattedAllColumn.add(_formatFactory.formatValue(value));
                }

            }
            sql.append(colName).append(",");
            if(value.equals(Value.newSequence())){
                valuesHolder.append(tableName).append(".").append("NEXTVAL").append(",");
            }else{
                valuesHolder.append("?,");
            }

        }
        sql = sql.deleteCharAt(sql.length()-1); // deleting the last ',' mark
        sql.append(")");
        valuesHolder = valuesHolder.deleteCharAt(valuesHolder.length()-1); // deleting the last ',' mark
        valuesHolder.append(")");
        sql.append(valuesHolder);
        Query query = new Query(sql.toString(),formattedAllColumn);
        return query;
    }

    /**
     * Builds a DELETE query
     * @param dataObj
     * @return
     * @throws Exception
     */
    public static Query getDeleteQuery(DataObject dataObj) throws Exception{
        InvocationMetadata meta = dataObj.getInvocationMetadata();
        Iterator where = dataObj.whereIterator();
        if(!where.hasNext()){
            throw new IllegalArgumentException("No WHERE clause found for DELETE!.");
        }
        // Building the WHERE clause
        String tableName = dataObj.getName();
        List formattedWhere = new ArrayList();
        StringBuffer sql = new StringBuffer("DELETE FROM ").
                append(tableName).append(" WHERE ");
        while(where.hasNext()){
            DataElement ele = (DataElement)where.next();
            Value value = ele.getValue();
            String name = ele.getName();
            formattedWhere.add(_formatFactory.formatValue(value));
            sql.append(name).append("=? AND "); // All WHERE conditons are AND
        }
        sql = sql.delete(sql.length()-4, sql.length()); // deleting the last 'AND'
        Query query = new Query(sql.toString(),formattedWhere);
        return query;
    }

}
