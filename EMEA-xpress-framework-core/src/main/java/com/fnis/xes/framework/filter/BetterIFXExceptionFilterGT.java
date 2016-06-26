package com.fnis.xes.framework.filter;

import com.fnf.jef.boc.RequestMessage;
import com.fnf.jef.boc.ResponseMessage;
import com.fnf.jef.boc.filter.RequestFilter;
import com.fnf.jef.boc.filter.RequestLink;
import com.fnf.xes.framework.ServiceException;
import com.fnf.xes.framework.management.monitoring.BaseErrorInfo;
import com.fnf.xes.framework.management.monitoring.MetricsPublisher;
import com.fnis.xes.framework.util.ErrWarnInfoMessage;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * @author e0112256 @author Basavaraj Patil (Updated to publish BaseErrorInfo)
 * GoTo IFXException2 Filter
 *
 * To catch errors which were not converted into XBO status objects
 *
 */
public class BetterIFXExceptionFilterGT implements RequestFilter {
    private static Logger log = Logger.getLogger(BetterIFXExceptionFilterGT.class);
    private static final String STATUS_START = "<Status xmlns=\"urn:ifxforum-org:XSD:1\">";
    private static final String STATUS_END = "</Status>";
    private static final String STATUS_CODE_START = "<StatusCode>";
    private static final String STATUS_CODE_END = "</StatusCode>";
    private static final String SERVER_STATUS_CODE_START = "<ServerStatusCode>";
    private static final String SERVER_STATUS_CODE_END = "</ServerStatusCode>";
    private static final String STATUS_SEVERITY = "<Severity>Error</Severity>";
    private static final String STATUS_SEVERITY_INFO = "<Severity>Info</Severity>";
    private static final String STATUS_DESC_START = "<StatusDesc>";
    private static final String STATUS_DESC_END = "</StatusDesc>";
    private static final String RQUID_START = "<RqUID xmlns=\"urn:ifxforum-org:XSD:1\">";
    private static final String RQUID_END = "</RqUID>";
    private static final String DEFAULT_RQUID = "00000000-0000-0000-0000-000000000000";
    private static final String ADDITIONAL_STATUS_TEMPLATE = "            <AdditionalStatus xmlns=\"http://www.fnf.com/xes\">\n" +
            "               <StatusCode xmlns=\"urn:ifxforum-org:XSD:1\">0</StatusCode>\n" +
            "               <ServerStatusCode xmlns=\"urn:ifxforum-org:XSD:1\">0</ServerStatusCode>\n" +
            "               <Severity xmlns=\"urn:ifxforum-org:XSD:1\">Info</Severity>\n" +
            "               <StatusDesc xmlns=\"urn:ifxforum-org:XSD:1\">TRANSACTION PROCESSING COMPLETE.</StatusDesc>\n" +
            "            </AdditionalStatus>\n" +
            "            <AdditionalStatus xmlns=\"http://www.fnf.com/xes\">\n" +
            "               <StatusCode xmlns=\"urn:ifxforum-org:XSD:1\">0</StatusCode>\n" +
            "               <ServerStatusCode xmlns=\"urn:ifxforum-org:XSD:1\">1120</ServerStatusCode>\n" +
            "               <Severity xmlns=\"urn:ifxforum-org:XSD:1\">Info</Severity>\n" +
            "               <StatusDesc xmlns=\"urn:ifxforum-org:XSD:1\">Success</StatusDesc>\n" +
            "            </AdditionalStatus>\n" +
            "            <AdditionalStatus xmlns=\"http://www.fnf.com/xes\">\n" +
            "               <StatusCode xmlns=\"urn:ifxforum-org:XSD:1\">-1000</StatusCode>\n" +
            "               <ServerStatusCode xmlns=\"urn:ifxforum-org:XSD:1\">1120</ServerStatusCode>\n" +
            "               <Severity xmlns=\"urn:ifxforum-org:XSD:1\">Info</Severity>\n" +
            "               <StatusDesc xmlns=\"urn:ifxforum-org:XSD:1\">No Records Found</StatusDesc>\n" +
            "            </AdditionalStatus>\n";
    private static final int GENERAL_ERROR_CODE = 100;
    private static final int STATUS_DESC_LENGTH = 255;
    private String m_sXMLResponse;
    private ErrWarnInfoMessage m_oFormatMessage;
    private boolean logExceptions = false;

    /**
     * Empty Constructor. If this filter ever needs an ability, simply add it to
     * this constructor.
     */
    public BetterIFXExceptionFilterGT(ErrWarnInfoMessage formatmessage) {
        m_oFormatMessage = formatmessage;
    }

    /**
     * doFilter
     *
     * @param requestMessage  <code>com.fnf.jef.boc.RequestMessage</code>
     * @param responseMessage <code>com.fnf.jef.boc.ResponseMessage</code>
     * @param requestLink     <code>com.fnf.jef.boc.filter.RequestLink</code>
     * @return void
     */
    public void doFilter(RequestMessage requestMessage,
                         ResponseMessage responseMessage, RequestLink requestLink)
            throws java.lang.Throwable {

        m_sXMLResponse = null;
        Object req = null;
        try {

            req = requestMessage.getObject();

            /*
             * Need to validate further if this is needed. if (req instanceof
             * java.lang.String) {
             *
             * String inputRequest = (String) req;
             * GetResponseStartAndEnd(inputRequest); GetRqUID(inputRequest); }
             */

            // Serve as a pass through
            requestLink.doFilter(requestMessage, responseMessage);

        } catch (ServiceException se) {
            log.info("Service Exception occured");
            if (logExceptions) {
               log.info("Logged exception", se);
            }
            createResponse(
                    se.getErrorCode(),
                    m_oFormatMessage.format(
                            se.getErrorCode(),
                            se.getMessage()), req, se);

        } catch (java.lang.Throwable e) {
            if (logExceptions) {
               log.info("Logged exception", e);
            }
            // create response
            createResponse(GENERAL_ERROR_CODE,
                    m_oFormatMessage.format(
                            GENERAL_ERROR_CODE,
                            "Unknown error"), req, e);

        } finally {
            if (m_sXMLResponse != null) {
                responseMessage.setObject(m_sXMLResponse);
            }
        }

    }

    private Pattern rqTagPattern = Pattern.compile("\\<\\s*(?:\\w+:){0,1}(\\w+)Rq\\s*[^\\<\\>]*\\>");

    protected String getResponseTag(String inputRequest) {

        String tag = "IFX";
        Matcher m = rqTagPattern.matcher(inputRequest);

        if (m.find()) {
            tag = m.group(1);
        }
        return tag;
    }

    private Pattern rqUIDPattern = Pattern.compile("\\<\\s*(?:\\w+:){0,1}RqUID[^\\>\\<]*\\>([^\\>\\<]*)\\<\\s*/RqUID[^\\>\\<]*\\>");

    public String getRqUID(String inputRequest) {

        Matcher m = rqUIDPattern.matcher(inputRequest);
        String rqUid = null;
        if (m.find()) {
            rqUid = m.group(1);
        }

        return rqUid;
    }

    /**
     * Initialize method
     *
     * @param properties
     * @throws com.fnf.jef.boc.BocException
     */
    public void initializeFilter(java.util.Properties properties)
            throws com.fnf.jef.boc.BocException {
       logExceptions = Boolean.parseBoolean(properties.getProperty("logExceptions", "false"));
    }

    /**
     * Wrapper to create the IFX 2.0 error response, wraps the server error code
     *
     * @param errorCode <code>String</code>
     * @param errorDesc <code>String</code>
     * @param request   <code>Object</code>
     * @param request   <code>String</code>
     * @return String
     */
    private void createResponse(int errorCode, String errorDesc, Object request, java.lang.Throwable th) {
        createResponse(errorCode, 0, errorDesc, request, th);
    }

    /**
     * Create the IFX 2.0 error response, needs the message information from the
     * request
     *
     * @param errorCode <code>String</code>
     * @param errorDesc <code>String</code>
     * @param request   <code>Object</code>
     * @param request   <code>String</code>
     * @return String
     */
    private void createResponse(int errorCode, int serverErrorCode,
                                String errorDesc, Object request, java.lang.Throwable th) {

        String rquidFromRequest = null;
        String tag = null;

        if (request instanceof java.lang.String) {

            String inputRequest = (String) request;
            tag = getResponseTag(inputRequest);
            rquidFromRequest = getRqUID(inputRequest);
        }

        if (MetricsPublisher.performanceLogger.isInfoEnabled()) {
            BaseErrorInfo errorInfo = new BaseErrorInfo(th);
            if (rquidFromRequest == null) {
                errorInfo.setRqUID(DEFAULT_RQUID);
            } else {
                errorInfo.setRqUID(rquidFromRequest);
            }
        }

        // GetResponseStartAndEnd

        StringBuilder status = new StringBuilder();

        StringBuilder rqUId = new StringBuilder();

        // Build Status
        String severityText = STATUS_SEVERITY;

        if(errorCode == 1120 || errorCode == 2300){
            status.append(STATUS_START + STATUS_CODE_START).append("0").append(STATUS_CODE_END);
            severityText = STATUS_SEVERITY_INFO;
        } else {
            status.append(STATUS_START + STATUS_CODE_START).append(errorCode).append(STATUS_CODE_END);
        }

        if (0 != serverErrorCode) {
            status.append(SERVER_STATUS_CODE_START).append(serverErrorCode).append(SERVER_STATUS_CODE_END);
        }

        if (errorDesc.length() >= STATUS_DESC_LENGTH) {
            status.append(severityText + STATUS_DESC_START).append(errorDesc.substring(0,STATUS_DESC_LENGTH - 1)).append(STATUS_DESC_END + STATUS_END);
        } else {
            status.append(severityText + STATUS_DESC_START).append(errorDesc).append(STATUS_DESC_END + STATUS_END);
        }

        // Additional Status
        if (errorCode == 1120 || errorCode == 2300){
            status.append(ADDITIONAL_STATUS_TEMPLATE);
        }

        // Build rqUId

        rqUId.append(RQUID_START);

        if (rquidFromRequest != null) {
            rqUId.append(rquidFromRequest);
        } else {
            rqUId.append(DEFAULT_RQUID); // default RqId
        }
        rqUId.append(RQUID_END);


        // there is no response. must create one
        m_sXMLResponse = "<" + tag + "Rs xmlns=\"http://www.fnf.com/xes\">" + status.toString()
                + rqUId.toString() + "</" + tag + "Rs>";

    }

    private void logme(int key, Throwable t) {
        logme(Integer.toString(key), null, t);
    }

    private void logme(int key, String msg, Throwable t) {
        logme(key, new String[]{msg}, t);
    }

    private void logme(int key, String[] msg, Throwable t) {
        logme(Integer.toString(key), msg, t);
    }

    private void logme(String key, String[] msg, Throwable t) {
        if ((null == msg) || (msg.length == 0)) {
            log.l7dlog(Priority.ERROR, key, t);
        } else {
            log.l7dlog(Priority.ERROR, key, msg, t);
        }
    }
}
