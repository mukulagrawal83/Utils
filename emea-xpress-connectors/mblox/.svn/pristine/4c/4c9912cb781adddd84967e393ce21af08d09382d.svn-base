package com.fisglobal.xpress.emea.sms.mblox.notifications;

import com.fisglobal.xpress.emea.mblox.NotificationRequestBuilder;
import com.fisglobal.xpress.emea.mblox.NotificationRequestTemplate;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static com.fisglobal.xpress.emea.mblox.NotificationRequestTemplate.*;
import static org.testng.Assert.*;
import static org.mockito.Mockito.*;

public class NotificationRequestBuilderTest {

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
	public void asString_should_return_string_representation_of_request() {
		// GIVEN
		// builder with existing template
		NotificationRequestBuilder builder = new NotificationRequestBuilder(notificationRequestTemplate);
		// AND expected result
		String expectedNotificationRequest = "abcdef";
		
		// WHEN notification request is built
		String actualNotificationRequest =  builder
												.withPartnerName("a")
												.withPartnerPassword("b")
												.withMessageId("c")
												.withMessage("d")
												.withSenderId("e")
												.withSubscriberNumber("f")
												.asString();
		
		// THEN it should be in expected format
		assertEquals(actualNotificationRequest, expectedNotificationRequest);
	}

}
