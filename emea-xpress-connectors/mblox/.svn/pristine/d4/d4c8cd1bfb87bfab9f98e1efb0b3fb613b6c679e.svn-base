package com.fisglobal.xpress.emea.sms.mblox.messaging;

import com.fisglobal.xpress.emea.mblox.MbloxMessaging;
import com.fisglobal.xpress.emea.mblox.NotificationDetails;
import com.fisglobal.xpress.emea.mblox.NotificationStatus;
import com.fisglobal.xpress.emea.sms.test.TestConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

@Test
@ContextConfiguration(locations = {TestConstants.SPRING_TEST_CONTEXT})
public class MbloxMessagingTest extends AbstractTestNGSpringContextTests {

	@Autowired(required=true)
    MbloxMessaging mbloxMessaging;
	
	@BeforeMethod
	void validateConfig() {
		assertNotNull(mbloxMessaging);
	}
	
	@Test
	public void sendNotification() {
		NotificationDetails notificationDetails = new NotificationDetails();
		
		String
			message = "test message",
			messageId = "88888888",
			senderId = "FIS",
			subscriberNumber = "48999999999";
		
		notificationDetails.setMessage(message);
		notificationDetails.setMessageId(messageId);
		notificationDetails.setSenderId(senderId);
		notificationDetails.setSubscriberNumber(subscriberNumber);
		
		NotificationStatus status = mbloxMessaging.sendNotification(notificationDetails);
		
		assertTrue(status.getMbloxConnectionStatus().isConnectionSuccess());
		assertEquals(status.getMbloxConnectionStatus().getResponseStatusCode(), new Integer("200"));
		
		assertEquals(status.getNotificationDetails().getMessage(), message);
		assertEquals(status.getNotificationDetails().getMessageId(), messageId);
		assertEquals(status.getNotificationDetails().getSenderId(), senderId);
		assertEquals(status.getNotificationDetails().getSubscriberNumber(), subscriberNumber);

		assertEquals(status.getNotificationRequestResultDetails().getNotificationResultCode(), 0);
		assertEquals(status.getNotificationRequestResultDetails().getRequestResultCode(), 0);
		assertEquals(status.getNotificationRequestResultDetails().getSubscriberResultCode(), 0);
		assertEquals(Boolean.valueOf(status.getNotificationRequestResultDetails().isRetry()), Boolean.FALSE);
	}
}
