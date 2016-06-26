package com.fisglobal.xpress.emea.sms.mblox.messaging.connector.http;

import com.fisglobal.xpress.emea.mblox.HttpConnectionStatus;
import com.fisglobal.xpress.emea.mblox.MbloxHttpClient;
import com.fisglobal.xpress.emea.sms.test.TestConstants;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import static org.testng.Assert.assertNotNull;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test
@ContextConfiguration(locations = {TestConstants.SPRING_TEST_CONTEXT})
public class MbloxHttpClientTest extends AbstractTestNGSpringContextTests {

	private static final Logger log = Logger.getLogger(MbloxHttpClientTest.class);
	
	@Autowired(required=true)
    MbloxHttpClient mbloxClient;
	
	@BeforeMethod
	void validateConfig() {
		assertNotNull(mbloxClient);
	}
	
	@Test(enabled=false)
	public void testSendNotificationRequestString() throws URISyntaxException {
		String dummyRequestContent = "DUMMY";
		
		String
			uri1 = "http://google.com",
			uri2 = "http://yahoo.com";
		
//		uri1 = URI.create("http://yahoo.com"),
//		uri2 = URI.create("http://yahoo.com");
		
		MbloxHttpClient mbloxHttpClient = new MbloxHttpClient();
                mbloxHttpClient.setMbloxUris(Arrays.asList(new URI(uri1), new URI(uri2)));
		
		HttpConnectionStatus connectionStatus = mbloxHttpClient.sendNotificationRequest(dummyRequestContent);
		
		log.info("Connection status:\n" + connectionStatus.toString());
	}
	
	@Test(enabled=false)
	public void testSendNotificationRequestString2() throws URISyntaxException {
		String dummyRequestContent = "DUMMY";

		String
			uri1 = "http://google.com",
			uri2 = "http://xml14.mblox.com:8180/send";
		
		MbloxHttpClient mbloxHttpClient = new MbloxHttpClient();
                mbloxHttpClient.setMbloxUris(Arrays.asList(new URI(uri1), new URI(uri2)));
		
		HttpConnectionStatus connectionStatus = mbloxHttpClient.sendNotificationRequest(dummyRequestContent);
		
		log.info("Connection status:\n" + connectionStatus.toString());
	}

	@Test
	public void testSendNotificationRequestString3() {
		String dummyRequestContent = "DUMMY";
		
		HttpConnectionStatus connectionStatus = mbloxClient.sendNotificationRequest(dummyRequestContent);
		
		log.info("Connection status:\n" + connectionStatus.toString());
	}
}
