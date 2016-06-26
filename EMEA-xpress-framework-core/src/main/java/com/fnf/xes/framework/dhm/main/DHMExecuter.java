package com.fnf.xes.framework.dhm.main;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

import com.fnf.xes.framework.ServiceException;
import com.fnf.xes.framework.dhm.data.format.FormatFactory;
import com.fnf.xes.framework.dhm.data.format.IFormatFactory;
import com.fnf.xes.framework.dhm.data.model.Action;
import com.fnf.xes.framework.dhm.data.model.DataElement;
import com.fnf.xes.framework.dhm.data.model.DataObject;
import com.fnf.xes.framework.dhm.data.ops.DataType;
import com.fnf.xes.framework.dhm.data.ops.FactoryType;
import com.fnf.xes.framework.dhm.mrpc.MRPCExecuter;
import com.fnf.xes.framework.dhm.services.ServiceExecuter;
import com.fnf.xes.framework.dhm.services.ServiceInput;
import com.fnf.xes.framework.dhm.sql.QueryExecuter;
import com.fnf.xes.framework.dhm.sql.SQLObject;
import org.apache.log4j.Logger;

/**
 *
 *
 * A service request deligator that deligates the request to appropriate
 * classes.
 *
 * @author NambiarSM
 */
public final class DHMExecuter {

    private static Logger logger = Logger.getLogger(DHMExecuter.class);
    private static IFormatFactory _formatFactory;
    static
    {
        _formatFactory = FormatFactory.getFormatFactory(FactoryType.PROFILE);
    }

    private DHMExecuter(){}
    /**
     * Execute a DataObject request. Dataobject is used to model a transaction which is either UPDATE, DELETE or INSERT.
     * It does not support SELECT operration.
     * @param dataObj
     * @param cache
     * @param con
     * @return
     * @throws DHMException
     */
    public static ResultSet execute(DataObject dataObj, Object cache, Connection con)throws DHMException{

        try {
            if(logger.isTraceEnabled()){
                logger.trace("+-----------------------------------+");
                logger.trace("Data Object " + dataObj.getName());
                logger.trace("User id     " + dataObj.getUserID());
                logger.trace("Action      " + dataObj.getAction());

                logger.trace("+------- Data Object params --------+");
                final Iterator iterator = dataObj.columnIterator();
                while(iterator.hasNext()){
                    String colValue;
                    DataElement ele = (DataElement)iterator.next();
                    logger.trace("Name  " + ele.getName());
                    if(ele.getValue().getDataType() != DataType.VARCHAR){
                        colValue = _formatFactory.formatValue(ele.getValue());
                    }
                    else{
                        colValue = ele.getValue().toString();
                    }
                    logger.trace("value " + colValue);
                }
                logger.trace("+-----------------------------------+");
            }

            if(dataObj.getAction().equals(Action.MRPC) || dataObj.getAction().equals(Action.MRPC_INQ)){
                DHMCache dhmCache = (DHMCache)cache;
                // MRPC requires the mrpc metadata from the DHM cache to execute the request
                if(dhmCache == null || dhmCache.getMrpcMetadata().size() ==0 ){
                    throw new DHMException(IdConstants.DHM_METADATA_NOT_FOUND,"MRPC",null);
                }
                CallableStatement stmt =  MRPCExecuter.prepareStatement(dataObj,dhmCache.getMrpcMetadata(),con);
                return StatementHelper.executeProcedure(stmt,0);
            }else{
                PreparedStatement stmt =  QueryExecuter.prepareStatement(dataObj,con);
                return StatementHelper.executeUpdate(stmt);
            }
        } catch (Exception e) {
            throw new DHMException(e);
        }
    }


    /**
     * Execute a Service request. ServiceInput is used to model a procedure type transaction. Inprofile its called stored procedure
     * which is a SQL transaction in a compiled form. Currently it supports SELECT and DELETE type transaction
     * @param inputReq
     * @param cache
     * @param con
     * @return ResultSet
     * @throws DHMException
     */
    public static ResultSet execute(ServiceInput inputReq, Object cache, Connection con) throws DHMException{
        try {
            DHMCache dhmCache = (DHMCache)cache;
            if(dhmCache == null || dhmCache.getServiceCatalog().size() == 0 ){
                throw new DHMException(IdConstants.DHM_METADATA_NOT_FOUND,"Service",null);
            }
            return ServiceExecuter.execute(inputReq,dhmCache,con);
        } catch (ServiceException se) {
            throw new DHMException(se.getErrorCode(),se.getMsgParams(),se);
        } catch (Exception e) {
            throw new DHMException(e);
        }
    }


    /**
     * Used to execute Service or Data Objects as a Bulk transacion
     *
     * @param inputReqMap
     * @param idList
     * @param cache
     * @param con
     * @param transactionLevel
     *            ALL_OR_NOTHING , STOP_ON_FIRST_ERROR & IGNORE_AND_CONTINUE
     * @return HashMap
     * @throws DHMException
     */
    public static java.util.HashMap executeBulk(HashMap inputReqMap,
                                                ArrayList idList, Object cache, Connection con, int transactionLevel)
            throws DHMException {
        try {
            HashMap outMap = new HashMap();
            if (inputReqMap.size() == 1) {
                ResultSet resultSet = null;
                List keyList = new ArrayList();
                keyList.addAll(inputReqMap.keySet());
                int correlationIdentifier = (Integer) keyList.get(0);
                Object input = inputReqMap.get(correlationIdentifier);
                try {
                    if (input instanceof DataObject) {
                        resultSet = execute((DataObject) input, cache, con);
                    } else if (input instanceof ServiceInput) {
                        resultSet = execute((ServiceInput) input, cache, con);
                    }
                    outMap.put(correlationIdentifier, resultSet);
                } catch (Exception e) {
                    outMap.put(correlationIdentifier, e);
                }
                return outMap;
            } else
                return StatementHelper.executeAggregateMRPC(inputReqMap, idList, cache, con, transactionLevel);

        } catch (DHMException e) {
            throw e;
        } catch (Exception e) {
            throw new DHMException(e);
        }
    }
    /**
     * Execute a Service request. SQLObject is used to model a SQL transaction in profile database. the supported transaction is SELECT
     * @param sqlObject
     * @param con
     * @return ResultSet
     * @throws DHMException
     */
    public static ResultSet execute(SQLObject sqlObject, Connection con) throws DHMException {
        try {

            if(logger.isTraceEnabled()){
                logger.trace("+-----------------------------------+");
                logger.trace("SQL Object " + sqlObject.getName());
                logger.trace("User id     " + sqlObject.getUserID());
                logger.trace("SQL      " + sqlObject.getSQL());

                logger.trace("+------- SQL Object params --------+");
                final ListIterator iter  = sqlObject.getParamList().listIterator();
                while(iter.hasNext()){
                    logger.trace("index :" + iter.nextIndex());
                    logger.trace("value :" + iter.next());
                }
                logger.trace("+-----------------------------------+");
            }

            return ServiceExecuter.execute(sqlObject, con);
        } catch (ServiceException se) {
            throw new DHMException(se.getErrorCode(),se.getMsgParams(),se);
        } catch (Exception e) {
            throw new DHMException(e);
        }
    }
}
