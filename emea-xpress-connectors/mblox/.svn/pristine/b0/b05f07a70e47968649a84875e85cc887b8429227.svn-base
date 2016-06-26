package com.fisglobal.xpress.emea.sms;

import com.fisglobal.xpress.emea.mblox.NotificationMessage;
import com.fisglobal.xpress.emea.mblox.NotificationUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.MessageChannel;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author e1050475
 */
@Test
@ContextConfiguration(locations = {"classpath:test-mblox-messaging-context.xml", "classpath:test-inbound-notifications-context.xml",
        "classpath:test-outbound-notifications-context.xml"})
public class TestFlow extends AbstractTestNGSpringContextTests {
    private static final Logger LOGGER = Logger.getLogger(TestFlow.class);

    @Autowired
    MessageChannel notificationChannel;

    @Test(enabled = false)
    public void ValidSMSValidatorTest() {
        int counter = 0;
        NotificationMessage notificationMessage = new NotificationMessage("1",
                "FIS", "654907263","31", "Sample text ", 9);
        notificationChannel.send(NotificationUtil.getMessage(notificationMessage));
        while (counter < 5) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Assert.fail();
            }
            counter++;
        }
    }
}
