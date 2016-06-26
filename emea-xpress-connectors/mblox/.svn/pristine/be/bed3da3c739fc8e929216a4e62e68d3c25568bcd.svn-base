package com.fisglobal.xpress.emea.sms.mblox.messaging.connector;

import com.fisglobal.xpress.emea.mblox.BaseMbloxConnector;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import java.net.URI;

public class BaseMbloxConnectorTest {
	private static final Logger log = Logger.getLogger(BaseMbloxConnectorTest.class);
			
	URI mbloxUri = URI.create("http://xml13.mblox.com:8180/send");
	
	@Test
	public void testSendNotificationRequestURINotificationRequestDetails() {
//		fail("Not yet implemented");
	}

	@Test
	public void sendNotificationRequest_should_send_string_request_to_given_URI() throws Exception {
		
		
		BaseMbloxConnector baseMbloxConnector = new BaseMbloxConnector();
		
		/*
			<NotificationRequest Version="3.5">
				<NotificationHeader>
					<PartnerName>FISGlobalMFA</PartnerName>
					<PartnerPassword>8TSs2nFz</PartnerPassword>
				</NotificationHeader>
				<NotificationList BatchID="1">
					<Notification SequenceNumber="1" MessageType="SMS" Format="UTF8">
						<Message>ąęćłóśżźćń</Message>
						<Profile>-1</Profile>
						<SenderID Type="Alpha">FIS</SenderID>
						<Subscriber>
							<SubscriberNumber>48669595695</SubscriberNumber>
						</Subscriber>
						<ContentType>-1</ContentType>
					</Notification>
				</NotificationList>
			</NotificationRequest>
		 */
		
		String notificationRequestStr =
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
//			"<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>"+
			"<NotificationRequest Version=\"3.5\">" +
					"<NotificationHeader>" +
						"<PartnerName>FISGlobalMFA</PartnerName>" +
						"<PartnerPassword>8TSs2nFz</PartnerPassword>" +
					"</NotificationHeader>" +
					"<NotificationList BatchID=\"1\">" +
						"<Notification SequenceNumber=\"1\" MessageType=\"SMS\" Format=\"UTF8\">" +
//						"<Notification SequenceNumber=\"1\" MessageType=\"SMS\">" +
//						"<Notification SequenceNumber=\"1\" MessageType=\"SMS\" Format=\"Unicode\">" +
							"<Message>xxx ąęćłóśżźćń</Message>" +
//							"<Message>xxx</Message>" +
							"<Profile>-1</Profile>" +
							"<SenderID Type=\"Alpha\">FIS</SenderID>" +
							"<Subscriber>" +
								"<SubscriberNumber>48669595695</SubscriberNumber>" +
							"</Subscriber>" +
							"<ContentType>-1</ContentType>" +
						"</Notification>" +
					"</NotificationList>" +
				"</NotificationRequest>";
		
//		Charset.forName("UTF-8").encode(notificationRequestStr);
		
//		String notificationRequestResultStr = baseMbloxConnector.sendNotificationRequestHttpClient(mbloxUri, notificationRequestStr);
//		log.info("Response:\n" + notificationRequestResultStr);
	}

}
