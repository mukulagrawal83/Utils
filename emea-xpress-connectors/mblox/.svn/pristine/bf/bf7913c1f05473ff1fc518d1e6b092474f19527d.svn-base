package com.fisglobal.xpress.emea.sms.mblox.notifications;

import com.fisglobal.xpress.emea.mblox.DefaultNotificationRequestTemplate;
import org.apache.commons.lang.StringUtils;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class DefaultNotificationRequestTemplateTest {
	@Test
	public void test1() {
		DefaultNotificationRequestTemplate defaultTemplate = new DefaultNotificationRequestTemplate();
		assertTrue(StringUtils.isNotBlank(defaultTemplate.getTemplate()));
	}
	
	@Test
	public void test2() {
		DefaultNotificationRequestTemplate defaultTemplate = new DefaultNotificationRequestTemplate("NotificationRequestTemplate.xml");
		assertTrue(StringUtils.isNotBlank(defaultTemplate.getTemplate()));
	}
}
