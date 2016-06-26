package com.fnf.xes.framework.pa;

import com.fnf.xes.framework.pa.usc.TransportLocal;
import com.fnf.xes.framework.pa.usc.TransportLocalHome;
import com.fnf.xes.framework.pa.usc.UscMessage;
import com.fnf.xes.framework.util.ExceptionUtil;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
//import java.util.Iterator;
import java.util.List;
import java.util.Properties;
//import java.util.ResourceBundle;
//import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;
import javax.transaction.Status;

import com.fnis.xes.framework.spring.XpressSpringFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.apache.log4j.NDC;
import org.apache.log4j.MDC;
import org.apache.log4j.helpers.Loader;
import org.apache.log4j.xml.DOMConfigurator;

import com.fnf.jef.boc.Container;
import com.fnf.jef.boc.MessageDispatcher;
import com.fnf.jef.boc.RequestMessage;
import com.fnf.jef.boc.ResponseMessage;
import com.fnf.xes.framework.IdConstants;
import com.fnf.xes.framework.ServiceException;
import com.fnf.xes.framework.management.monitoring.EndpointMetric;
import com.fnf.xes.framework.management.monitoring.EventManager;
import com.fnf.xes.framework.management.monitoring.MetricsPublisher;
import com.fnf.xes.framework.util.ErrWarnInfoMessage;
import com.fnf.xes.framework.util.FastInfoSetUtility;
import com.fnf.xes.framework.webservices.axis2.ServiceBase;
import com.fnf.xes.framework.logging.XESLogFormatter;

/**
 * User: lc21878
 * Date: 16/09/15
 * Time: 17:57
 */
public class HttpMsgAdaptor extends HttpServlet implements Servlet {

    /*
     * Instance of an unmanaged logger for this class.
     */
    private static Logger logger = Logger.getLogger(HttpMsgAdaptor.class);

    /*
     * Configurable properties
     */
    private static String BOC_CONFIG_ENV = "httpAdaptorBocConfigName";
    private static String ENDPOINT_SSB_JNDI_ENV = "endpointSsbJndi";
    private static String ENABLE_UT_ENV = "enableUserTransaction";
    private static String MAX_POOL_SIZE_ENV = "poolSize.max";
    private static String MIN_POOL_SIZE_ENV = "poolSize.min";
    private static int DEFAULT_MAX_POOL_SIZE = 10;
    private static int DEFAULT_MIN_POOL_SIZE = 1;
    private UserTransaction m_ut = null;
    private String m_ServletName = "";
    private GenericObjectPool bocConfigPool = null;
    private boolean isDispatchLocal = true;
    private String endpointSsbJndi = null;
    private TransportLocalHome transportLocalHome = null;
    /*
    * Hold on to init errors to report in the "doGet" method.
    */
    private boolean errorOnInit = false;
    private Exception initException;

    /*
     * Error handling resource bundle ability
     */
    ErrWarnInfoMessage ewim = null;

    /**
     * This method cannot be used to send service requests. This method is used
     * to retrieve servlet status and tweak configuration.
     *
     * @see javax.servlet.http.HttpServlet#void
     *      (javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html");

        /*
         * TODO: Update this method to allow pool size to be adjusted on the fly.
         */



        PrintWriter out = resp.getWriter();
        out.println("<HTML><HEAD><TITLE>" + m_ServletName + " Servlet</TITLE>" +
                "</HEAD><BODY>XES Framework Http Adaptor Status<br><br>");

        if (errorOnInit) {
            out.println("Error occurred durring Servlet initialization.<br>");
            reportException(out, initException);
        } else {
            if (isDispatchLocal) {
                Container container = null;

                String testBoc = req.getParameter("testBoc");
                String maxpool = req.getParameter("maxpool");
                String poolsettings = req.getParameter("poolsettings");

                if (null != testBoc) {
                    try {
                        out.println("Attempting to fetch BOC out of pool: ok.<br>");
                        container = (Container) bocConfigPool.borrowObject();
                        container.getObjectEx(MessageDispatcher.class);
                        out.println("Fetch BOC out of pool: ok.<br>");
                    } catch (Exception ex) {
                        out.println(
                                "Error occurred durring fetch of BOC container.<br>");
                        reportException(out, ex);
                    } finally {
                        try {
                            if (container != null) {
                                bocConfigPool.returnObject(container);
                            }
                        } catch (Exception e) {
                            if (logger.isEnabledFor(Level.ERROR)) {
                                logger.error("HttpMsgAdaptor - Error returning BOC container to pool",
                                        e);
                            }
                        }
                    }
                }

                if (null != maxpool) {
                    try {
                        int max = Integer.parseInt(maxpool);

                        /*
                         * Don't do something silly like call this under high load
                         * or in production.  You will likely see smoke.
                         * We don't want to add a sync block here becase that would impact
                         * performance of the service calls and we only need this functionality
                         * for tuning the pool size in a test environment.
                         */
                        if (max <= bocConfigPool.getMaxTotal()) {
                            out.println("Cannot set max pool size to a number lower than its active objects. <br/>");
                        } else {
                            bocConfigPool.setMaxTotal(max);
                            out.println("Pool size adjusted successfully. <br/>");
                        }

                    } catch (Exception e) {
                        out.println("Failed changing pool size.<br/>");
                        reportException(out, e);
                    }
                }

                if (null != poolsettings) {
                    out.println("Max active objects: " + bocConfigPool.getMaxTotal() + "<br/>");
                    out.println("Max inactive objects: " + bocConfigPool.getMaxIdle() + "<br/>");
                    out.println("Min inactive objects: " + bocConfigPool.getMinIdle() + "<br/>");
                    out.println("Current active objects: " + bocConfigPool.getNumActive() + "<br/>");
                    out.println("Current idle objects: " + bocConfigPool.getNumIdle() + "<br/>");
                }
            } else {
                out.println("Endpoint delegate: " + endpointSsbJndi + "<br/>");
            }
        }
        out.println("</BODY></HTML>");
        out.close();
    }

    /**
     * DOCUMENT ME!
     *
     * @param out
     *            DOCUMENT ME!
     * @param ex
     *            DOCUMENT ME!
     *
     * @throws IOException
     *             DOCUMENT ME!
     */
    private void reportException(PrintWriter out, Exception ex)
            throws IOException {
        out.println("Exception: " + ex.toString() + "<br>");
        out.println("Exception Message: " + ex.getMessage() + "<br>");
        out.println("Exception Stack Elements:<br>");

        StackTraceElement[] steArray = ex.getStackTrace();

        for (int ii = 0; (ii < steArray.length) && (ii < 10); ii++) {
            StackTraceElement ste = steArray[ii];
            out.println(ste.toString() + "<br>");
        }
    }

    /**
     * HTTP entrypoint into XES. HTTP Service requests go through this method
     *
     * @see javax.servlet.http.HttpServlet#void
     *      (javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        /*
         * Setup a unique id for this thread's logger. The id is passed in from
         * the client (header=NDC) and will group all of the log statements for
         * the current thread with this id. Doing this allows one to search the
         * logs by this id and find the logs for a particular transaction.
         */
        String ndc = null;
        String responseString = null;
        int status = 200;
        String contentType = req.getContentType().toLowerCase();
        EndpointMetric endpointStats = new EndpointMetric("HttpMsgAdaptor");
        try {
            XESLogFormatter.publishDefaultMDC();
            MDC.put("APPLICATION_ID", "TPSS:XPRESS");

            ndc = req.getHeader("NDC");
            // if client does not provide an ID then generate one.
            if (ndc == null) {
                ndc = com.fnf.jef.util.GuidGen.getGuid();
            }
            NDC.push(m_ServletName + "/" + ndc);

            logger.debug("Service request started.");
            //

            // Create and start the performance timers.
            endpointStats.startTime();
            // Read input into string
            String requestString = readHttpRequest(req, ndc);

            // if content type is a string type then lets do the conversion.
            if(requestString.startsWith("requestTestXML=")){
                requestString = requestString.substring(15, requestString.length());
                contentType="text/xml";
            }

            endpointStats.setRequestSize(requestString.length());

            if (isDispatchLocal) {
                responseString = dispatchLocal(requestString, req.getServletPath(), contentType, ndc);
            } else {
                responseString = dispatchDelegated(requestString, req.getServletPath(), contentType, ndc);
            }
            //
            writeHttpResponse(resp, responseString, contentType, status, ndc);
        } catch (ServiceException se) {
            /*
             * This is where we handle how to return the exceptions back to the
             * client.
             */
            logger.error("Will be writing error response.");
            logger.error("Response error code: " + se.getErrorCode());
            for (int i = 0; i < se.getMsgParams().length; i++) {
                logger.error("Response Error Param(" + i + "): " + se.getMsgParams()[i]);
            }
            String errDesc = ewim.format(se.getErrorCode(), se.getMsgParams());
            responseString = se.getErrorCode() + ": " + errDesc;
            status = 500;
            writeHttpResponse(resp, responseString, contentType, status, ndc);

        } finally {
            endpointStats.setResponseSize(responseString.length());
            handleEndPointStats(endpointStats);
            if (null != ndc) {
                NDC.pop();
                NDC.remove();
            }
        }
    }

    /**
     * Call through stateless session bean.
     *
     * @param msgPayload
     * @param msgId
     * @param contentType
     * @return
     * @throws ServiceException
     */
    private String dispatchDelegated(String msgPayload, String msgId,
                                     String contentType, String ndc) throws ServiceException {
        String responseString = null;
        TransportLocal transportLocal = null;
        try {
            transportLocal = this.transportLocalHome.create();
        } catch (Exception e) {
            String[] errAttribs = {ndc, "Error creating transport home: Reason:" + ExceptionUtil.rollupExceptionMessage(e)};
            String err = ewim.format(IdConstants.HTTPMSGADAPTER_ERROR_CREATING_TRANSPORT, errAttribs);
            logger.error(err, e);
            //TODO: need a defined error code here.
            throw new ServiceException(IdConstants.HTTPMSGADAPTER_ERROR_CREATING_TRANSPORT, errAttribs, e);

        }
        if ((null == msgId) || (msgId.length() == 0)) {
            msgId = "http";
        }
        UscMessage uscResponseMsg = null;
        try {
            UscMessage uscRequestMsg = new UscMessage(msgId,msgPayload);
            uscRequestMsg.setProperty("contentType", contentType);
            uscResponseMsg = transportLocal.send("default", uscRequestMsg);
        } catch (Exception e) {
            String[] errAttribs = {ndc, "Error processing Service request: Reason:" + ExceptionUtil.rollupExceptionMessage(e)};
            String err = ewim.format(IdConstants.HTTPMSGADAPTER_MESSAGE_DISPATCH_EXCEPTION, errAttribs);
            logger.error(err, e);
            //TODO: need a defined error code here.
            throw new ServiceException(IdConstants.HTTPMSGADAPTER_MESSAGE_DISPATCH_EXCEPTION, errAttribs, e);
        }
        responseString = (String) uscResponseMsg.getPayload();
        if (null == responseString) {
            responseString = "0: Request submitted to XES, returned success.";
        }
        return responseString;
    }

    /**
     *
     * @param msgPayload
     * @param msgId
     * @param contentType
     * @param ndc
     * @throws ServiceException
     */
    private String dispatchLocal(String msgPayload, String msgId,
                                 String contentType, String ndc) throws ServiceException {

        Container container = null;
        try {
            container = fetchContainer(ndc);

            MessageDispatcher messageDispatcher = fetchMessageDispatcher(
                    container, ndc);
            // String msgId = req.getServletPath();
            if ((null == msgId) || (msgId.length() == 0)) {
                msgId = "http";
            }
            RequestMessage requestMsg = new RequestMessage(msgId, msgPayload);
            requestMsg.setProperty("contentType", contentType);
            ResponseMessage responseMsg = null;
            try {
                startOptionalUserTransaction(ndc);
                logger.debug("Dispatching request.");
                responseMsg = messageDispatcher.dispatch(requestMsg);
                // throw if no response object is returned.
                if (null == responseMsg) {
                    throwNullResponse(msgPayload, ndc);
                }
                commitOptionalUserTransaction(ndc);
            } catch (Exception e) {
                //Error thrown in call to message dispatcher
                rollbackOptionalUserTransaction(ndc);
                String[] errAttribs = {ndc, "Error processing Service request: Reason:" + ExceptionUtil.rollupExceptionMessage(e)};
                String err = ewim.format(IdConstants.HTTPMSGADAPTER_MESSAGE_DISPATCH_EXCEPTION, errAttribs);
                logger.error(err, e);
                //TODO: need a defined error code here.
                throw new ServiceException(IdConstants.HTTPMSGADAPTER_MESSAGE_DISPATCH_EXCEPTION, errAttribs, e);
            }

            /*
             * It is possible that the request could simply be a system event
             * and no response is expected. If the response should not be null
             * then the client will have to handle the issue
             */
            String responseString = (String) responseMsg.getObject();
            if (null == responseString) {
                /*
                 * String err =
                 * ewim.format(IdConstants.HTTPMSGADAPTER_NO_RESPONSE_PAYLOAD,
                 * ndc); logger.error(err); logger.error("Request that was being
                 * processed during error condition: "+msgObj); throw new
                 * ServiceException
                 * (IdConstants.HTTPMSGADAPTER_NO_RESPONSE_PAYLOAD, ndc);
                 */
                responseString = "0: Request submitted to XES, returned success.";
            }
            return responseString;
        } finally {
            returnContainer(container, ndc);
        }
    }

    /**
     * If no BOC response message is returned from dispatch(...) then log and
     * throw an exception
     *
     * @param requestString
     */
    private void throwNullResponse(String requestString, String ndc)
            throws ServiceException {
        String err = ewim.format(IdConstants.HTTPMSGADAPTER_NO_RESPONSE, ndc);
        logger.error(err);
        logger.error("Request that was being processed during error condition: " + requestString);
        throw new ServiceException(IdConstants.HTTPMSGADAPTER_NO_RESPONSE, ndc);
    }

    /**
     * Start a transaction if a usertransaction has been configured
     *
     * @throws com.fnf.xes.framework.ServiceException
     */
    private void startOptionalUserTransaction(String ndc)
            throws ServiceException {
        if (m_ut != null) {
            logger.debug("starting user transaction");
            try {
                m_ut.begin();
            } catch (Exception e) {
                String[] errAttribs = {ndc, e.getMessage()};
                String err = ewim.format(
                        IdConstants.HTTPMSGADAPTER_UT_BEGIN_EXCEPTION,
                        errAttribs);
                logger.error(err);
                throw new ServiceException(
                        IdConstants.HTTPMSGADAPTER_UT_BEGIN_EXCEPTION,
                        errAttribs);
            }
        }
    }

    /**
     * Commit a transaction if a usertransaction has been configured
     *
     * @param ndc
     * @throws com.fnf.xes.framework.ServiceException
     */
    private void commitOptionalUserTransaction(String ndc)
            throws ServiceException {
        if (m_ut != null) {
            try {
                if (m_ut.getStatus() == Status.STATUS_ACTIVE) {
                    logger.debug("Commiting the user transaction.");
                    m_ut.commit();
                }
            } catch (Exception ee) {
                String[] errAttribs = {ndc, ee.getMessage()};
                String err = ewim.format(IdConstants.HTTPMSGADAPTER_UT_COMMIT_EXCEPTION, errAttribs);
                logger.error(err, ee);
                throw new ServiceException(IdConstants.HTTPMSGADAPTER_UT_COMMIT_EXCEPTION, errAttribs, ee);

                /*
                * If the commit failed, should we try to rollback?  It will likely fail as well.
                */
            }
        }
    }

    /**
     * Rollback transaction if Usertransaction has been configured
     */
    private void rollbackOptionalUserTransaction(String ndc)
            throws ServiceException {
        if (m_ut != null) {
            try {
                if (m_ut.getStatus() == Status.STATUS_ACTIVE) {
                    logger.error("Rolling the user transaction back.");
                    m_ut.rollback();
                }
            } catch (Exception ee) {
                String[] rberrAttribs = {ndc, ee.getMessage()};
                String rberr = ewim.format(
                        IdConstants.HTTPMSGADAPTER_UT_ROLLBACK_EXCEPTION,
                        rberrAttribs);
                logger.error(rberr);
                throw new ServiceException(
                        IdConstants.HTTPMSGADAPTER_UT_ROLLBACK_EXCEPTION,
                        rberrAttribs, ee);
            }
        }
    }

    /**
     * fetch a message dispatcher from the container and report any errros.
     *
     * @param container
     * @return
     * @throws ServiceException
     */
    private MessageDispatcher fetchMessageDispatcher(Container container,
                                                     String ndc) throws ServiceException {
        MessageDispatcher messageDispatcher = null;
        try {
            logger.debug("Attempting to lookup the message dispatcher.");
            messageDispatcher = (MessageDispatcher) container.getObjectEx(MessageDispatcher.class);
        } catch (Exception e) {
            String[] errAttribs = {ndc, "Error fetching MessageDispatcher from BOC. Reason:"+ExceptionUtil.rollupExceptionMessage(e)};
            String err = ewim.format(
                    IdConstants.HTTPMSGADAPTER_NO_MESSAGE_DISPATCHER_IN_BOC,
                    errAttribs);
            logger.error(err, e);
            throw new ServiceException(
                    IdConstants.HTTPMSGADAPTER_NO_MESSAGE_DISPATCHER_IN_BOC,
                    errAttribs, e);
        }

        if (null == messageDispatcher) {
            String err = ewim.format(
                    IdConstants.HTTPMSGADAPTER_NO_MESSAGE_DISPATCHER_IN_BOC,
                    ndc);
            logger.error(err);
            throw new ServiceException(
                    IdConstants.HTTPMSGADAPTER_NO_MESSAGE_DISPATCHER_IN_BOC,
                    ndc);
        }

        logger.debug("located a message dispatcher");
        return messageDispatcher;
    }

    /**
     * fetch a boc container from the pool
     *
     * @return
     * @throws com.fnf.xes.framework.ServiceException
     */
    private Container fetchContainer(String ndc) throws ServiceException {
        Container container = null;
        try {
            container = (Container) bocConfigPool.borrowObject();
        } catch (Exception e) {
            String[] errAttribs = {ndc, "Error fetching BOC container from pool. Reason:" + ExceptionUtil.rollupExceptionMessage(e)};
            String err = ewim.format(
                    IdConstants.HTTPMSGADAPTER_GET_BOC_FROM_POOL_EXCEPTION,
                    errAttribs);
            logger.error(err, e);
            throw new ServiceException(
                    IdConstants.HTTPMSGADAPTER_GET_BOC_FROM_POOL_EXCEPTION,
                    errAttribs, e);
        }

        return container;
    }

    /**
     * return a container back to the pool.
     *
     * @param container
     */
    private void returnContainer(Container container, String ndc)
            throws ServiceException {
        if (container != null) {
            try {
                bocConfigPool.returnObject(container);
            } catch (Exception e) {
                String[] errAttribs = {ndc, e.getMessage()};
                String err = ewim.format(
                        IdConstants.HTTPMSGADAPTER_GET_BOC_FROM_POOL_EXCEPTION,
                        errAttribs);
                logger.error(err, e);
                throw new ServiceException(
                        IdConstants.HTTPMSGADAPTER_GET_BOC_FROM_POOL_EXCEPTION,
                        errAttribs, e);
            }
        }
    }

    /**
     *
     * @param httpResp
     * @param responseString
     * @param contentType
     * @param status
     * @param ndc
     */
    private void writeHttpResponse(HttpServletResponse httpResp,
                                   String responseString, String contentType, int status, String ndc)
            throws ServletException {
        if (logger.isInfoEnabled()) {
            if (null != ndc) {
                logger.info("Service request started. "+ndc);
            } else {
                logger.info("Service request started.");
            }
            logger.debug("writing response.");
            logger.debug(responseString);
            logger.debug("Response size: " + responseString.length());
        }
        httpResp.setStatus(status);
        if (status == 200) {
            httpResp.setContentType(contentType);
        } else {
            /*
             * Have to set this to text/plain if a error response is being
             * returned. Some application servers will replace the response
             * contents if this does not happen.
             */
            httpResp.setContentType("text/plain");
        }
        BufferedWriter responseWriter = null;
        try {
            responseWriter = new BufferedWriter(new OutputStreamWriter(httpResp.getOutputStream(), Charset.forName("UTF-8")));// SHOULD
            // this
            // really be
            // hardcoded
            // to UTF-8?
            /*
            * TODO: What about FastInfoSet? We, questionably, check for it in
            * the request should we not do the same for the response? Is there
            * anything that has to be done? The FastInfoSetUtility just does a
            * straight conversion from byte[] to char[] to String and visa
            * versa.
            */
            responseWriter.write(responseString);
            responseWriter.flush();
        } catch (IOException ioe) {
            String[] errAttribs = {ndc, "IO Error writing HTTP response",
                    contentType
            };
            String err = ewim.format(IdConstants.HTTPMSGADAPTER_RQ_READ_ERROR,
                    errAttribs);
            logger.error(err);
            throw new ServletException(err);
        } finally {
            close(responseWriter);
        }
    }

    /**
     * Read byte in from http request as either characters or FastInfoSet. Http
     * Request Content Length is *not* used because the length could be unknown
     * (-1) or set incorrectly.
     *
     * @param httpReq
     * @return
     */
    private String readHttpRequest(HttpServletRequest httpReq, String ndc)
            throws ServiceException {
        String msgString = null;
        String contentType = httpReq.getContentType();
        if (contentType.indexOf("text") >= 0) {
            // jvincent 1/22/08 to handle special chars
            BufferedReader buffReader = null;
            try {
                String encodeValue = "UTF-8";
                if (httpReq.getHeader("charset") != null) {
                    encodeValue = httpReq.getHeader("charset");
                }

                InputStreamReader requestReader = new InputStreamReader(httpReq.getInputStream(), Charset.forName(encodeValue));
                buffReader = new BufferedReader(requestReader);

                StringBuffer request = new StringBuffer(2 * 1024);
                char[] inBuff = new char[2 * 1024];
                int len = 0;
                while ((len = buffReader.read(inBuff, 0, inBuff.length)) != -1) {
                    request.append(inBuff, 0, len);
                }

                msgString = request.toString();


            } catch (IOException ioe) {
                String[] errAttribs = {ndc, "IO Error reading HTTP reaquest",
                        contentType};
                String err = ewim.format(
                        IdConstants.HTTPMSGADAPTER_RS_WRITE_ERROR, errAttribs);
                logger.error(err);
                throw new ServiceException(
                        IdConstants.HTTPMSGADAPTER_RS_WRITE_ERROR, errAttribs);
            } finally {
                close(buffReader);
            }

        } else if (contentType.indexOf("fastinfoset") >= 0) {
            ByteArrayOutputStream bout = null;
            BufferedInputStream bis = null;
            try {
                ServletInputStream inStream = httpReq.getInputStream();
                bout = new ByteArrayOutputStream(2 * 1024);
                bis = new BufferedInputStream(inStream);
                byte[] ioBuff = new byte[2 * 1024];
                int len = 0;
                while ((len = bis.read(ioBuff)) != -1) {
                    bout.write(ioBuff, 0, len);
                }
                byte[] inData = bout.toByteArray();
                /*
                 * Shouldn't a downstream boc filter be able to handle this.
                 * While the hell do we waste processing time here to basically
                 * change character encoding? The parsing filter should be able
                 * to accept FIS without first having to convert it to plain
                 * XML.
                 */
                msgString = FastInfoSetUtility.byteToString(inData);
            } catch (IOException ioe) {
                String[] errAttribs = {ndc, "IO Error reading reaquest",
                        contentType
                };
                String err = ewim.format(
                        IdConstants.HTTPMSGADAPTER_RQ_READ_ERROR, errAttribs);
                logger.error(err);
                throw new ServiceException(
                        IdConstants.HTTPMSGADAPTER_RQ_READ_ERROR, errAttribs);
            } finally {
                close(bis);
                close(bout);
            }
        } else {
            /*
             * what kind of request is this? Should we be presumptious and
             * assume that it is readable like text? Nah, lets blow up!
             */
            String[] errAttribs = {ndc, "Invalid Content Type", contentType};
            String err = ewim.format(IdConstants.HTTPMSGADAPTER_RQ_READ_ERROR,
                    errAttribs);
            logger.error(err);
            throw new ServiceException(
                    IdConstants.HTTPMSGADAPTER_RQ_READ_ERROR, errAttribs);
        }
        return msgString;
    }


    /**
     *
     * @param endpointStats
     */
    private void handleEndPointStats(EndpointMetric endpointStats) {
        try {
            endpointStats.stopTime();
            if (logger.isDebugEnabled()) {
                logger.debug("Service Time: " + endpointStats.getElapsedTime());
            }

            Boolean success = (Boolean) MetricsPublisher.getInstance().getProperty(MetricsPublisher.SUCCESSFUL);
            if (Boolean.TRUE.equals(success)) {
                endpointStats.setSuccess(true);
            } else {
                endpointStats.setSuccess(false);
            }

            EventManager em = EventManager.getInstance();
            em.processEvents();
        } catch (Throwable t) {
            logger.error("Following execption occured in hanleEndPointStats:" + t.getMessage(),t);
        }

    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getServletInfo() {
        /*
         * This probably should be updated by the build process to have up to
         * date and accurate information here.
         */
        return "XES Framework Http Adaptor version 1.0, copyright 2005, Fidelity National Information Services Inc.";
    }

    /**
     * This method is called by the servlet container when the servlet is first
     * created. Below are the properties that it expects and/or can be
     * configured.
     *
     * BOC_CONFIG_ENV <required> - name of the boc configuration file
     * ENABLE_UT_ENV <optional> - is this servlet required to start a user
     * transaction. MAX_POOL_SIZE_ENV <optional> - maximum size of the boc pool
     * MIN_POOL_SIZE_ENV <optional> - minimum size of the boc pool
     *
     * @param ServletConfig -
     *            Servlet parameters specified in web.xml
     *
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        XESLogFormatter.publishDefaultMDC();
        MDC.put("APPLICATION_ID", "TPSS:XPRESS");

        m_ServletName = config.getServletName();
        NDC.push(m_ServletName);

        try {

            /*
             * Initialize the resource bundle for error reporting
             */
            ewim = new ErrWarnInfoMessage();
            Properties props = new Properties();
            props.put("1", "com.fnf.xes.framework.ErrWarnInfo");
            try {
                ewim.initializeAbility(props);
            } catch (Exception e) {
                String err = "Could not load resource bundle for error handling.";
                logger.fatal(err);
                ServletException se = new ServletException(err, e);
                initException = e;
                errorOnInit = true;
                throw se;
            }

            /*
             * Check for the name of the boc configuration file. This is a
             * required init parameter
             */
            String bocConfigName = config.getInitParameter(BOC_CONFIG_ENV);
            endpointSsbJndi = config.getInitParameter(ENDPOINT_SSB_JNDI_ENV);
            if ((bocConfigName == null) && (endpointSsbJndi == null)) {
                String err = "Required Servlet parameter '" + BOC_CONFIG_ENV + "' or '" + ENDPOINT_SSB_JNDI_ENV + "'is not specified in web.xml.";
                logger.fatal(err);
                ServletException e = new ServletException(err);
                initException = e;
                errorOnInit = true;
                throw e;

            }
            if (bocConfigName != null) {
                //if the BOC file is registered in jefConfig.properties, use it and share
                // the same pool instance; otherwise, create a new BOC pool instance
                try {
//					bocConfigPool = ServiceBase.ini(bocConfigName);
                    bocConfigPool = XpressSpringFactory.getContextPool(bocConfigName);
                } catch (Exception e) {
                    logger.fatal(e);
                    throw new ServletException(e);
                }
            }
            else {
                configureForDelegatedDispatch(endpointSsbJndi, config);
            }


            logger.info(m_ServletName + " Servlet initialized succesfully with the following values:");
            logger.info("Boc Config = " + bocConfigName);
            logger.info("User Transactions Enabled: " + (m_ut != null));

        } finally {
            // just need to be able to pop the NDC.
            NDC.pop();
            NDC.remove();
        }
    }

    /**
     *
     * @throws ServletException
     */
    private void configureForLocalDispatch(String bocConfigName,
                                           ServletConfig config) throws ServletException {
        isDispatchLocal = true;
        /*
         * Determine the maximum size of the boc pool
         */
        int bocPoolMax;
        String strBocPoolMax = config.getInitParameter(MAX_POOL_SIZE_ENV);
        try {
            bocPoolMax = Integer.parseInt(strBocPoolMax);
        } catch (Exception e) {
            String reason = null;
            if (e instanceof NullPointerException) {
                reason = MAX_POOL_SIZE_ENV + " init parameter not specified.";
            } else if (e instanceof NumberFormatException) {
                reason = MAX_POOL_SIZE_ENV + " init parameter has an invalid value of " + strBocPoolMax;
            } else {
                reason = "Unknown - " + e.getMessage();
            }
            bocPoolMax = DEFAULT_MAX_POOL_SIZE;
            logger.warn("Using default max pool size. Reason: " + reason);
        }

        /*
         * Determine the min size of the boc pool
         */
        int bocPoolMin;
        String strBocPoolMin = config.getInitParameter(MIN_POOL_SIZE_ENV);
        try {
            bocPoolMin = Integer.parseInt(strBocPoolMin);
        } catch (Exception e) {
            String reason = null;
            if (e instanceof NullPointerException) {
                reason = MIN_POOL_SIZE_ENV + " init parameter not specified.";
            } else if (e instanceof NumberFormatException) {
                reason = MIN_POOL_SIZE_ENV + " init parameter has an invalid value of " + strBocPoolMin;
            } else {
                reason = "Unknown - " + e.getMessage();
            }
            /*
             * The default will be to have the max and min be the same. Later
             * the pool will be spun up to the min to get everything warmed up.
             */
            bocPoolMin = bocPoolMax;
            logger.warn("Using default min pool size. Reason: " + reason);
        }

        /*
         * Initialize the boc pool
         */
        try {
            GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
            poolConfig.setMaxTotal(bocPoolMax);
            poolConfig.setMaxIdle(-1);
            poolConfig.setMaxWaitMillis(-1);
            poolConfig.setMinIdle(bocPoolMin);
            bocConfigPool = new GenericObjectPool(new BocConfigPoolFactory(
                    bocConfigName), poolConfig);
        } catch (Exception e) {
            String err = "Errors occured while initializing the BOC pool. Reason:" + ExceptionUtil.rollupExceptionMessage(e);
            logger.fatal(err);
            ServletException se = new ServletException(err, e);
            initException = e;
            errorOnInit = true;
            throw se;
        }

        /*
         * Should this servlet make use of UserTransaction?
         */
        String strEnableUT = config.getInitParameter(ENABLE_UT_ENV);
        if ((null != strEnableUT) && ((strEnableUT.equalsIgnoreCase("Y")) || (strEnableUT.equalsIgnoreCase("true")))) {
            logger.info("UserTransaction have been configured to be on");
            try {
                Context ctx = new InitialContext();
                Object o = ctx.lookup("java:comp/UserTransaction");
                logger.debug("UserTransaction: " + o);
                m_ut = (UserTransaction) o;
            } catch (Exception e) {
                /*
                 * We have to throw a fatal error because there was supposed to
                 * be a user transaction for this servlet which means that we
                 * cannot ignore it if one cannot be created.
                 */
                m_ut = null;
                String err = "Errors occured when looking up UserTransaction. " + e.getMessage();
                logger.fatal(err);
                ServletException se = new ServletException(err, e);
                initException = e;
                errorOnInit = true;
                throw se;
            }
        }
        logger.info(m_ServletName + " Servlet initialized succesfully with the following values:");
        logger.info("Boc Config = " + bocConfigName);
        logger.info("Max Pool Size: " + bocPoolMax);
        logger.info("Min Pool Size: " + bocPoolMin);
        logger.info("User Transactions Enabled: " + (m_ut != null));

        logger.info("Spinning up "+bocPoolMin+" boc containers.");
        List tmpBocs = new ArrayList(bocPoolMin);
        for (int i=0; i<bocPoolMin; i++) {
            try {
                tmpBocs.add((Container)bocConfigPool.borrowObject());
            } catch(Exception e) {
                logger.fatal("Failed to warmup the boc pool. "+e.getMessage(), e);
                initException = e;
                errorOnInit = true;
                break;
            }
        }

        for (int i=0; i<tmpBocs.size(); i++) {
            try {
                if (tmpBocs.get(i)!=null)
                    bocConfigPool.returnObject(tmpBocs.get(i));
            } catch(Exception e) {
                logger.fatal("Failed returning warmup instances to the pool. "+e.getMessage(), e);
                initException = e;
                errorOnInit = true;
            }
        }
        if (errorOnInit) {
            throw new ServletException("An error occured while warming up the boc pool.", initException);
        }



        logger.info("BOC pool successfuly warmed up.");

    }

    /**
     *
     * @param endpointSsbJndi
     * @param config
     * @throws ServletException
     */
    private void configureForDelegatedDispatch(String endpointSsbJndi,
                                               ServletConfig config) throws ServletException {
        isDispatchLocal = false;
        logger.info("Initializing HttpMsgAdapter with EJB delegate: " + endpointSsbJndi);
        Context context = null;
        try {
            context = new InitialContext();
            transportLocalHome = (TransportLocalHome) context.lookup (endpointSsbJndi);
        } catch (NameNotFoundException nnfe) {
            String[] errAttribs = {"Endpoint Session Bean delegate not found. Reason:"+ ExceptionUtil.rollupExceptionMessage(nnfe),
                    endpointSsbJndi};
            String err = ewim.format(
                    IdConstants.HTTPMSGADAPTER_ENDPOINT_NAME_NOT_FOUND, errAttribs);
            logger.error(err);
            initException = new ServletException(err);
            errorOnInit = true;
            throw new ServletException(initException);
        } catch (NamingException ne) {
            String[] errAttribs = {"HttpMsgAdapter JNDI error. Reason:"+ ExceptionUtil.rollupExceptionMessage(ne),
                    endpointSsbJndi};
            String err = ewim.format(
                    IdConstants.HTTPMSGADAPTER_GENERAL_JNDI_ERROR, errAttribs);
            logger.error(err);
            initException = new ServletException(err);
            errorOnInit = true;
            throw new ServletException(initException);
        } finally {
            close(context);
        }
    }
    /**
     *
     * @param baos
     */
    private void close(ByteArrayOutputStream baos) {
        if (baos != null) {
            try {
                baos.close();
            } catch (Exception ignore) {
            }
        }
    }

    /**
     *
     * @param bos
     */
    private void close(BufferedInputStream bos) {
        if (bos != null) {
            try {
                bos.close();
            } catch (Exception ignore) {
            }
        }
    }

    /**
     *
     * @param bufferedReader
     */
    private void close(BufferedReader bufferedReader) {
        if (bufferedReader != null) {
            try {
                bufferedReader.close();
            } catch (Exception ignore) {
            }
        }
    }

    /**
     *
     * @param bufferedWriter
     * @return
     */
    private void close(BufferedWriter bufferedWriter) {
        if (bufferedWriter != null) {
            try {
                bufferedWriter.close();
            } catch (Exception ignore) {
            }
        }
    }
    /**
     *
     */
    private void close(Context context) {
        if (context != null) {
            try {
                context.close();
            } catch (Exception ignore) { }
        }
    }

}
