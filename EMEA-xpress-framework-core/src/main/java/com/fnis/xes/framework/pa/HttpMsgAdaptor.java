package com.fnis.xes.framework.pa;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fnis.xes.framework.spring.XpressSpringFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.Loader;
import org.apache.log4j.xml.DOMConfigurator;

import com.fnf.jef.boc.Container;
import com.fnf.jef.boc.MessageDispatcher;
import com.fnf.jef.boc.RequestMessage;
import com.fnf.jef.boc.ResponseMessage;
import com.fnf.xes.framework.management.monitoring.EndpointMetric;
import com.fnf.xes.framework.management.monitoring.EventManager;
import com.fnf.xes.framework.management.monitoring.MetricsPublisher;
import com.fnf.xes.framework.util.FastInfoSetUtility;
import com.fnis.xes.framework.webservices.axis2.ServiceBase;

/**
 * Created with IntelliJ IDEA.
 * User: lc21878
 * Date: 16/09/15
 * Time: 17:53
 */
public class HttpMsgAdaptor extends HttpServlet implements Servlet {
    private static Logger logger = Logger.getLogger(HttpMsgAdaptor.class);

    private static String BOC_CONFIG_ENV = "httpAdaptorBocConfigName";

    private boolean errorOnInit = false;

    private Exception initException;

    private GenericObjectPool bocConfigPool;

    /**
     * @see javax.servlet.http.HttpServlet#void
     *      (javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");

        PrintWriter out = resp.getWriter();
        out.println("<HTML><HEAD><TITLE>XES Framework Http Adaptor</TITLE>" + "</HEAD><BODY>XES Framework Http Adaptor Status<br><br>");

        if (errorOnInit) {
            out.println("Error occurred durring Servlet initialization.<br>");
            reportException(out, initException);
        } else {
            Container container = null;

            try {
                out.println("Attempting to fetch BOC out of pool: ok.<br>");
                container = (Container) bocConfigPool.borrowObject();

                MessageDispatcher messageDispatcher = (MessageDispatcher) container.getObjectEx(MessageDispatcher.class);
                out.println("Fetch BOC out of pool: ok.<br>");
            } catch (Exception ex) {
                out.println("Error occurred durring fetch of BOC container.<br>");
                reportException(out, ex);
            } finally {
                try {
                    if (container != null) {
                        bocConfigPool.returnObject(container);
                    }
                } catch (Exception e) {
                    if (logger.isEnabledFor(Level.ERROR))
                        logger.error("HttpMsgAdaptor - Error returning BOC container to pool", e);
                }
            }
        }

        out.println("</BODY></HTML>");
        out.close();
    }

    /**
     * DOCUMENT ME!
     *
     * @param out
     *          DOCUMENT ME!
     * @param ex
     *          DOCUMENT ME!
     *
     * @throws IOException
     *           DOCUMENT ME!
     */
    private void reportException(PrintWriter out, Exception ex) throws IOException {
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
     * @see javax.servlet.http.HttpServlet#void
     *      (javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Start time for purposes of gathering performance metrics
        java.util.Date startTime = new java.util.Date();

        if (logger.isEnabledFor(Level.INFO)) {
            logger.info("Start of Event Listener Servlet");
        }

        // get a handle to the boc
        ServletInputStream inStream = req.getInputStream();

        String contentType = req.getContentType().toLowerCase();

        // read input data
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        int c;
        while ((c = inStream.read()) != -1) {
            bout.write(c);
        }
        byte[] inData = bout.toByteArray();

        Object msgObj = inData;

        // if content type is a string type then lets do the conversion.
        if (contentType.indexOf("text") >= 0) {
            String rqMsg = new String(inData);
            if(rqMsg.startsWith("requestTestXML=")){
                rqMsg = rqMsg.substring(15, rqMsg.length());
            }
            msgObj = rqMsg;
        } else if (contentType.indexOf("fastinfoset") >= 0) {
            msgObj = FastInfoSetUtility.byteToString(inData);
        }

        // default response msg
        String errorResponseXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><HttpMsgAdaptor>Error processing message</HttpMsgAdaptor>";
        Object responseObj = null;
        Container container = null;
        String response = "0: Request submitted to XES, returned success - HTTP Status 200";
        int status = 200;
        EndpointMetric endpointStats = new EndpointMetric("HttpMsgAdaptor");
        endpointStats.startTime();
        endpointStats.setRequestSize(inData.length);
        try {
            container = (Container) bocConfigPool.borrowObject();

            MessageDispatcher messageDispatcher = (MessageDispatcher) container.getObjectEx(MessageDispatcher.class);
            String msgId = req.getServletPath();

            if ((msgId == null) || (msgId.length() == 0)) {
                msgId = "http";
            } else if (contentType.indexOf("fastinfoset") >= 0) {
                msgObj = FastInfoSetUtility.byteToString(inData);
            }

            RequestMessage requestMsg = new RequestMessage(msgId, msgObj);
            requestMsg.setProperty("contentType", contentType);

            ResponseMessage responseMsg = messageDispatcher.dispatch(requestMsg);
            responseObj = responseMsg.getObject();
            if (responseObj == null) {

                status = 500;
                response = "2: Request submitted to XES, not returned - HTTP Status 500";
            } else {
                response = (String) responseObj;
            }
            if (logger.isEnabledFor(Level.INFO)) {
                logger.info("Response object is" + responseObj);
            }
        } catch (Exception ex) {
            status = 500;
            response = "3: Unable to submit request to XES - HTTP Status 500";

            if (logger.isEnabledFor(Level.ERROR))
                logger.error("XES HttpMsgAdaptor: Error processing request: " + ex.getMessage(), ex);
        } finally {
            if (responseObj == null)
                responseObj = errorResponseXml;
            endpointStats.setResponseSize(responseObj.toString().length());
            handleEndPointStats(endpointStats);
            try {
                if (container != null) {
                    bocConfigPool.returnObject(container);
                }

            } catch (Exception e) {
                if (logger.isEnabledFor(Level.ERROR))
                    logger.error("HttpMsgAdaptor - Error returning BOC container to pool", e);
            }
        }

        resp.setStatus(status);

        String svPath = req.getServletPath();
        if(svPath!=null && svPath.equals("/IFXTestClient")){
            // for text client set the content type as XML for pretty printing
            resp.setContentType("text/xml");
        }else{
            // what ever the client sent in that is what we are going to send out.
            resp.setContentType(contentType);
        }


        // Always return UTF-8 encoded
        BufferedWriter responseWriter = new BufferedWriter(new OutputStreamWriter(resp.getOutputStream(), Charset.forName("UTF-8")));
        responseWriter.write(response);
        responseWriter.flush();
        responseWriter.close();

        // Since the sole use of the metrics are for writing data to the info log,
        // this entire
        // block can be wrapped in a single test for the logging level
        if (logger.isEnabledFor(Level.INFO)) {
            logger.info("End of Event Listener Servlet");

            java.util.Date endTime = new java.util.Date();
            logger.info("Time taken by servlet in seconds is " + (endTime.getTime() - startTime.getTime()) / 1000.0);
        }
    }

    private void handleEndPointStats(EndpointMetric endpointStats) {
        try {
            endpointStats.stopTime();
            Boolean success = (Boolean) MetricsPublisher.getInstance().getProperty(MetricsPublisher.SUCCESSFUL);
            if (Boolean.TRUE.equals(success)) {
                endpointStats.setSuccess(true);
            } else {
                endpointStats.setSuccess(false);
            }
            EventManager em = EventManager.getInstance();
            em.processEvents();
        } catch (Throwable t) {
            logger.error("Following execption occured in handleEndPointStats:" + t.getMessage());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getServletInfo() {
        return "XES Framework Http Adaptor version 1.0, copyright 2005, Fidelity National Information Services Inc.";
    }

    /**
     * This init is where agent manager should be started and in turn the agents
     * are booted by the manager.
     *
     * @param config
     *          DOCUMENT ME!
     */

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String bocConfigName = config.getInitParameter(BOC_CONFIG_ENV);

        if (bocConfigName == null) {
            if (logger.isEnabledFor(Level.ERROR))
                logger.error("Required Servlet parameter '" + BOC_CONFIG_ENV + "' is not specified in web.xml.");
            throw new ServletException("Required Servlet parameter '" + BOC_CONFIG_ENV + "' is not specified in web.xml.");
        }


        try {
            if (logger.isInfoEnabled())
                logger.info("XES HttpMsgAdaptor Servlet: initializaing with configuration: " + bocConfigName);

//	      bocConfigPool = ServiceBase.ini(bocConfigName);
            bocConfigPool = XpressSpringFactory.getContextPool(bocConfigName);

        } catch (Exception ex) {
            initException = ex;
            errorOnInit = true;
            if (logger.isEnabledFor(Level.ERROR))
                logger.error(ex.toString(), ex);
        }
    }
}
