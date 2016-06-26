package com.fisglobal.xpress.emea.sms.mblox.notifications;

import com.fisglobal.xpress.emea.mblox.NotificationRequestResultDetails;
import com.fisglobal.xpress.emea.mblox.NotificationRequestResultMarshaller;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import java.net.URLEncoder;

import static org.testng.Assert.assertEquals;

public class NotificationRequestResultMarshallerTest {

	private static final Logger log = Logger.getLogger(NotificationRequestResultMarshallerTest.class);
	
	private final String notificationRequestResultXmlTemplate =
		"<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>" + "\n" +
		"<NotificationRequestResult Version=\"3.5\">" + "\n" +
		"    <NotificationResultHeader>" + "\n" +
		"        <PartnerName>FISGlobalMFA</PartnerName>" + "\n" +
		"        <PartnerRef></PartnerRef>" + "\n" +
		"        <RequestResultCode>${RequestResultCode}</RequestResultCode>" + "\n" +
		"        <RequestResultText>${RequestResultText}</RequestResultText>" + "\n" +
		"    </NotificationResultHeader>" + "\n" +
		"    <NotificationResultList>" + "\n" +
		"        <NotificationResult SequenceNumber=\"1\">" + "\n" +
		"            <NotificationResultCode>${NotificationResultCode}</NotificationResultCode>" + "\n" +
		"            <NotificationResultText>${NotificationResultText}</NotificationResultText>" + "\n" +
		"            <SubscriberResult>" + "\n" +
		"                <SubscriberNumber>666</SubscriberNumber>" + "\n" +
		"                <SubscriberResultCode>${SubscriberResultCode}</SubscriberResultCode>" + "\n" +
		"                <SubscriberResultText>${SubscriberResultText}</SubscriberResultText>" + "\n" +
		"                <Retry>${Retry}</Retry>" + "\n" +
		"                <Operator>0</Operator>" + "\n" +
		"            </SubscriberResult>" + "\n" +
		"        </NotificationResult>" + "\n" +
		"    </NotificationResultList>" + "\n" +
		"</NotificationRequestResult>";
	
	@Test
	public void unmarshallString_should_unmarshall_default_ok_result() {
		// GIVEN

		// expected object
		
		int requestResultCode			= 0;						//RequestResultCode
		String requestResultText		= "requestResultText";		//RequestResultText
		int notificationResultCode		= 0;						//NotificationResultCode
		String notificationResultText 	= "notificationResultText";	//NotificationResultText
		int subscriberResultCode		= 0;						//SubscriberResultCode
		String subscriberResultText		= "subscriberResultText";	//SubscriberResultText
		boolean retry					= false;					//Retry
		
		NotificationRequestResultDetails expectedNotificationReqResultDetails = new NotificationRequestResultDetails();
		expectedNotificationReqResultDetails.setRequestResultCode(requestResultCode);
		expectedNotificationReqResultDetails.setRequestResultText(requestResultText);
		expectedNotificationReqResultDetails.setNotificationResultCode(notificationResultCode);
		expectedNotificationReqResultDetails.setNotificationResultText(notificationResultText);
		expectedNotificationReqResultDetails.setSubscriberResultCode(subscriberResultCode);
		expectedNotificationReqResultDetails.setSubscriberResultText(subscriberResultText);
		expectedNotificationReqResultDetails.setRetry(retry);
		
		// AND default ok notification request result
		
		String notificationRequestResultXmlStr = notificationRequestResultXmlTemplate;
		notificationRequestResultXmlStr = notificationRequestResultXmlStr.replace(placeholder("RequestResultCode"), requestResultCode + "");
		notificationRequestResultXmlStr = notificationRequestResultXmlStr.replace(placeholder("RequestResultText"), requestResultText + "");
		notificationRequestResultXmlStr = notificationRequestResultXmlStr.replace(placeholder("NotificationResultCode"), notificationResultCode + "");
		notificationRequestResultXmlStr = notificationRequestResultXmlStr.replace(placeholder("NotificationResultText"), notificationResultText + "");
		notificationRequestResultXmlStr = notificationRequestResultXmlStr.replace(placeholder("SubscriberResultCode"), subscriberResultCode + "");
		notificationRequestResultXmlStr = notificationRequestResultXmlStr.replace(placeholder("SubscriberResultText"), subscriberResultText + "");
		notificationRequestResultXmlStr = notificationRequestResultXmlStr.replace(placeholder("Retry"), retry ? "1" : "0");
		
		log.info("Testing notificationRequestResultXmlStr:\n" + notificationRequestResultXmlStr);
		
		// THEN unmarshalled notification request result is equal to expected object
		NotificationRequestResultDetails
			actualNotificationReqResultDetails = new NotificationRequestResultMarshaller().unmarshall(notificationRequestResultXmlStr);
		
		assertEquals(actualNotificationReqResultDetails, expectedNotificationReqResultDetails);
	}
	
	String notificationRequestResultFormattingErrorTemplate =
		"<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>" + "\n" +
		"<NotificationRequestResult Version=\"3.5\">" + "\n" +
		"    <NotificationResultHeader>" + "\n" +
		"        <PartnerName>FISGlobalMFA</PartnerName>" + "\n" +
		"        <PartnerRef></PartnerRef>" + "\n" +
		"        <RequestResultCode>${RequestResultCode}</RequestResultCode>" + "\n" +
		"        <RequestResultText>${RequestResultText}</RequestResultText>" + "\n" +
		"    </NotificationResultHeader>" + "\n" +
		"</NotificationRequestResult>";
	
	@Test
	public void unmarshallString_should_unmarshall_formatting_error_result() {
		// GIVEN

		// expected object
		
		int requestResultCode			= 3;						//RequestResultCode
		String requestResultText		= URLEncoder.encode("Incorrect <PartnerName> or <PartnerPassword> values");		//RequestResultText
		
		NotificationRequestResultDetails expectedNotificationReqResultDetails = new NotificationRequestResultDetails();
		expectedNotificationReqResultDetails.setRequestResultCode(requestResultCode);
		expectedNotificationReqResultDetails.setRequestResultText(requestResultText);
		
		// AND default ok notification request result
		
		String notificationRequestResultXmlStr = notificationRequestResultFormattingErrorTemplate;
		notificationRequestResultXmlStr = notificationRequestResultXmlStr.replace(placeholder("RequestResultCode"), requestResultCode + "");
		notificationRequestResultXmlStr = notificationRequestResultXmlStr.replace(placeholder("RequestResultText"), requestResultText + "");
		
		log.info("Testing notificationRequestResultXmlStr:\n" + notificationRequestResultXmlStr);
		
		// THEN unmarshalled notification request result is equal to expected object
		NotificationRequestResultDetails
			actualNotificationReqResultDetails = new NotificationRequestResultMarshaller().unmarshall(notificationRequestResultXmlStr);
		
		assertEquals(actualNotificationReqResultDetails, expectedNotificationReqResultDetails);
	}
	
	String notificationRequestResultMalformedErrorTemplate = 
		"<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>" + "\n" +
		"<NotificationRequestResult Version=\"3.5\">" + "\n" +
		"    <NotificationResultHeader>" + "\n" +
		"        <PartnerName>FISGlobalMFA</PartnerName>" + "\n" +
		"        <PartnerRef></PartnerRef>" + "\n" +
		"        <RequestResultCode>${RequestResultCode}</RequestResultCode>" + "\n" +
		"        <RequestResultText>${RequestResultText}</RequestResultText>" + "\n" +
		"    </NotificationResultHeader>" + "\n" +
		"    <NotificationResultList>" + "\n" +
		"        <NotificationResult SequenceNumber=\"1\">" + "\n" +
		"            <NotificationResultCode>${NotificationResultCode}</NotificationResultCode>" + "\n" +
		"            <NotificationResultText>${NotificationResultText}</NotificationResultText>" + "\n" +
		"        </NotificationResult>" + "\n" +
		"    </NotificationResultList>" + "\n" +
		"</NotificationRequestResult>";
	
	@Test
	public void unmarshallString_should_unmarshall_malformed_error_result() {
		
		// GIVEN

		// expected object
		
		int requestResultCode			= 0;						//RequestResultCode
		String requestResultText		= "OK";		//RequestResultText
		int notificationResultCode		= 2;						//NotificationResultCode
		String notificationResultText 	= URLEncoder.encode("invalid Sequence or no <Message> element");	//NotificationResultText
		
		NotificationRequestResultDetails expectedNotificationReqResultDetails = new NotificationRequestResultDetails();
		expectedNotificationReqResultDetails.setRequestResultCode(requestResultCode);
		expectedNotificationReqResultDetails.setRequestResultText(requestResultText);
		expectedNotificationReqResultDetails.setNotificationResultCode(notificationResultCode);
		expectedNotificationReqResultDetails.setNotificationResultText(notificationResultText);
		
		// AND default ok notification request result
		
		String notificationRequestResultXmlStr = notificationRequestResultMalformedErrorTemplate;
		notificationRequestResultXmlStr = notificationRequestResultXmlStr.replace(placeholder("RequestResultCode"), requestResultCode + "");
		notificationRequestResultXmlStr = notificationRequestResultXmlStr.replace(placeholder("RequestResultText"), requestResultText + "");
		notificationRequestResultXmlStr = notificationRequestResultXmlStr.replace(placeholder("NotificationResultCode"), notificationResultCode + "");
		notificationRequestResultXmlStr = notificationRequestResultXmlStr.replace(placeholder("NotificationResultText"), notificationResultText + "");
		
		log.info("Testing notificationRequestResultXmlStr:\n" + notificationRequestResultXmlStr);
		
		// THEN unmarshalled notification request result is equal to expected object
		NotificationRequestResultDetails
			actualNotificationReqResultDetails = new NotificationRequestResultMarshaller().unmarshall(notificationRequestResultXmlStr);
		
		assertEquals(actualNotificationReqResultDetails, expectedNotificationReqResultDetails);
	}
	
	String retryTemplate = "<Retry>${Retry}</Retry>";
	
	@Test
	public void unmarshallString_should_unmarshall_retry() {
		// GIVEN

		// expected object
		
		boolean retry = true;
		
		NotificationRequestResultDetails expectedNotificationReqResultDetails = new NotificationRequestResultDetails();
		expectedNotificationReqResultDetails.setRetry(retry);
		
		// AND default ok notification request result
		
		String retryXmlStr = retryTemplate;
		retryXmlStr = retryXmlStr.replace(placeholder("Retry"), retry ? "1" : "0");
		
		log.info("Testing retryXmlStr:\n" + retryXmlStr);
		
		// THEN unmarshalled notification request result is equal to expected object
		NotificationRequestResultDetails
			actualNotificationReqResultDetails = new NotificationRequestResultMarshaller().unmarshall(retryXmlStr);
		
		assertEquals(actualNotificationReqResultDetails, expectedNotificationReqResultDetails);
	}
	
	String placeholder(String str) {
		return "${" + str + "}";
	}
}
