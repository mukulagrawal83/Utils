package com.fisglobal.xpress.emea.sms.mblox.notifications;

import com.fisglobal.xpress.emea.mblox.*;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static com.fisglobal.xpress.emea.mblox.NotificationRequestTemplate.MESSAGE_ID_PLACEHOLDER;
import static com.fisglobal.xpress.emea.mblox.NotificationRequestTemplate.MESSAGE_PLACEHOLDER;
import static com.fisglobal.xpress.emea.mblox.NotificationRequestTemplate.PARTNER_NAME_PLACEHOLDER;
import static com.fisglobal.xpress.emea.mblox.NotificationRequestTemplate.PARTNER_PASSWORD_PLACEHOLDER;
import static com.fisglobal.xpress.emea.mblox.NotificationRequestTemplate.SENDER_ID_PLACEHOLDER;
import static com.fisglobal.xpress.emea.mblox.NotificationRequestTemplate.SUBSCRIBER_NUMBER_PLACEHOLDER;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

public class NotificationRequestMarshallerTest {

	NotificationRequestTemplate notificationRequestTemplate;
	
	@BeforeTest
	void prepareSimpleTemplate() {
		String template = 	PARTNER_NAME_PLACEHOLDER +
							PARTNER_PASSWORD_PLACEHOLDER +
							MESSAGE_ID_PLACEHOLDER +
							MESSAGE_PLACEHOLDER +
							SENDER_ID_PLACEHOLDER +
							SUBSCRIBER_NUMBER_PLACEHOLDER;
		
		notificationRequestTemplate = mock(NotificationRequestTemplate.class);
		when(notificationRequestTemplate.getTemplate()).thenReturn(template);
		
		assertEquals(notificationRequestTemplate.getTemplate(), template);
	}
	
	@Test
	public void marshallToString_should_marshall_using_notification_request_details() {
		// GIVEN
		// expected result
		String expectedNotificationRequest = "abcdef";
		
		// marshaller with existing template
		NotificationRequestMarshaller marshaller = new NotificationRequestMarshaller(notificationRequestTemplate);
		
		// AND predefined notification and header details
		NotificationHeaderDetails notificationHdrDetails = new NotificationHeaderDetails();
		
		notificationHdrDetails.setPartnerName("a");
		notificationHdrDetails.setPartnerPassword("b");
		
		NotificationDetails notificationDetails = new NotificationDetails();
		
		notificationDetails.setMessageId("c");
		notificationDetails.setMessage("d");
		notificationDetails.setSenderId("e");
		notificationDetails.setSubscriberNumber("f");
		
		NotificationRequestDetails
			notificationRequestDetails = new NotificationRequestDetails(notificationHdrDetails, notificationDetails);
		
		// WHEN notification request is marshalled
		String actualNotificationRequest = marshaller.marshallToString(notificationRequestDetails);
		
		// THEN it should be in expected format
		assertEquals(actualNotificationRequest, expectedNotificationRequest);
	}
}
