package com.fisglobal.xpress.emea.mblox;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;

public class NotificationRequestResultMarshaller {

    private static final Logger log = Logger.getLogger(NotificationRequestResultMarshaller.class);

    private static final String RETRY_FLAG_VALUE = "1";

    public NotificationRequestResultDetails unmarshall(String notificationRequestResultXmlStr) {
        if (StringUtils.isBlank(notificationRequestResultXmlStr))
            throw new IllegalArgumentException("notificationRequestResultXmlStr is blank");

        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            docBuilderFactory.setNamespaceAware(true);
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document domTree = docBuilder.parse(new InputSource(new StringReader(notificationRequestResultXmlStr)));
            return unmarshall(domTree);

        } catch (Exception e) {
            String msg = "Could not parse notification request result xml:\n" + notificationRequestResultXmlStr + "\nPossible cause: " + e.getMessage();
            log.error(msg);
            throw new RuntimeException(msg, e);
        }
    }

    public NotificationRequestResultDetails unmarshall(Document docTree) {
        if (Utils.isNull(docTree)) throw new IllegalArgumentException("docTree is null");

        XPath xpath = XPathFactory.newInstance().newXPath();

        try {
            Double requestResultCode = Utils.nanToNull((Double) xpath.evaluate("//RequestResultCode", docTree, XPathConstants.NUMBER));
            String requestResultText = Utils.blankToNull((String) xpath.evaluate("//RequestResultText", docTree, XPathConstants.STRING));
            Double notificationResultCode = Utils.nanToNull((Double) xpath.evaluate("//NotificationResultCode", docTree, XPathConstants.NUMBER));
            String notificationResultText = Utils.blankToNull((String) xpath.evaluate("//NotificationResultText", docTree, XPathConstants.STRING));
            Double subscriberResultCode = Utils.nanToNull((Double) xpath.evaluate("//SubscriberResultCode", docTree, XPathConstants.NUMBER));
            String subscriberResultText = Utils.blankToNull((String) xpath.evaluate("//SubscriberResultText", docTree, XPathConstants.STRING));
            String retry = Utils.blankToNull((String) xpath.evaluate("//Retry", docTree, XPathConstants.STRING));

            NotificationRequestResultDetails notificationReqResultDetails = new NotificationRequestResultDetails();

            notificationReqResultDetails.setRequestResultCode(requestResultCode != null ? requestResultCode.intValue() : null);
            notificationReqResultDetails.setRequestResultText(requestResultText);
            notificationReqResultDetails.setNotificationResultCode(notificationResultCode != null ? notificationResultCode.intValue() : null);
            notificationReqResultDetails.setNotificationResultText(notificationResultText);
            notificationReqResultDetails.setSubscriberResultCode(subscriberResultCode != null ? subscriberResultCode.intValue() : null);
            notificationReqResultDetails.setSubscriberResultText(subscriberResultText);
            notificationReqResultDetails.setRetry(retry != null ? retry.equals(RETRY_FLAG_VALUE) : null);

            return notificationReqResultDetails;

        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }
}
