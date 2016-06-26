package com.fisglobal.xpress.emea.sms;

import com.fisglobal.xpress.emea.mblox.BlackOutTime;
import com.fisglobal.xpress.emea.mblox.NotificationMessage;
import com.fisglobal.xpress.emea.mblox.NotificationMessageException;
import com.fisglobal.xpress.emea.mblox.NotificationService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.jms.JMSException;
import java.util.Calendar;
import java.util.UUID;

/**
 * @author e1050475
 */
@Test
@ContextConfiguration(locations = {"classpath:test-common-configuration.xml",
        "classpath:test-mblox-messaging-context.xml",
        "classpath*:/xpress/ifx2_spring_http_client_config.xml"
})
public class NotificationServiceTest extends AbstractTestNGSpringContextTests {

    private static final Logger LOGGER = Logger.getLogger(NotificationServiceTest.class);

    @Autowired
    NotificationService notificationService;

    public void ValidSMSValidatorTest() {
        boolean errorOccurred = false;
        NotificationMessage notificationMessage = new NotificationMessage(UUID.randomUUID().toString(),
                "FIS", "654907263","31", "Sample text ", 9);
        try {
            notificationService.sendSMS(notificationMessage);
        } catch (NotificationMessageException notificationException) {
            errorOccurred = true;
        } catch (JMSException e) {
            errorOccurred = true;
        }
        Assert.assertEquals(errorOccurred, false);
    }

    public void ValidateSMSGlobalBlackOutTimeTest() {
        NotificationMessage notificationMessage1 = null;
        NotificationMessage notificationMessage = new NotificationMessage(UUID.randomUUID().toString(),
                "FIS", "07478761452","44", "Sample text ", 9);
        try {
            notificationMessage1 = notificationService.sendSMS(notificationMessage);
        } catch (NotificationMessageException notificationException) {
            Assert.fail();
        } catch (JMSException e) {
            Assert.fail();
        }

        final long timeDeltaMillis = 1000, // 1 second
                blackOutFrom = (Long) notificationMessage1.getProperties().get(NotificationMessage.BLACK_OUT_FROM),
                blackOutTo = (Long) notificationMessage1.getProperties().get(NotificationMessage.BLACK_OUT_TO);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        LOGGER.debug(String.format("Comparing NotificationMessage.BLACK_OUT_FROM actual: %s expected: %s delta: %s", blackOutFrom, calendar.getTimeInMillis(), timeDeltaMillis));
        Assert.assertTrue(blackOutFrom >= calendar.getTimeInMillis() - timeDeltaMillis, "From time is invalid");
        Assert.assertTrue(blackOutFrom <= calendar.getTimeInMillis() + timeDeltaMillis, "From time is invalid");

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);

        LOGGER.debug(String.format("Comparing NotificationMessage.BLACK_OUT_TO actual: %s expected: %s delta: %s", blackOutFrom, calendar.getTimeInMillis(), timeDeltaMillis));
        Assert.assertTrue(blackOutTo >= calendar.getTimeInMillis() - timeDeltaMillis, "To time is invalid");
        Assert.assertTrue(blackOutTo <= calendar.getTimeInMillis() + timeDeltaMillis, "To time is invalid");
    }

    public void ValidateSMSBlackOutTimeTest() {
        NotificationMessage notificationMessage1 = null;
        NotificationMessage notificationMessage = new NotificationMessage(UUID.randomUUID().toString(),
                "FIS", "07478761452","44", "Sample text ", 9);
        try {
            BlackOutTime blackOutTime = new BlackOutTime("19:00", "22:00");
            notificationMessage.getProperties().put(NotificationMessage.BLACK_OUT_PERIOD, blackOutTime);
            notificationMessage1 = notificationService.sendSMS(notificationMessage);
        } catch (NotificationMessageException notificationException) {
            Assert.fail();
        } catch (JMSException e) {
            Assert.fail();
        }

        final long timeDeltaMillis = 1000, // 1 second
                blackOutFrom = (Long) notificationMessage1.getProperties().get(NotificationMessage.BLACK_OUT_FROM),
                blackOutTo = (Long) notificationMessage1.getProperties().get(NotificationMessage.BLACK_OUT_TO);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 19);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        LOGGER.debug(String.format("Comparing NotificationMessage.BLACK_OUT_FROM actual: %s expected: %s delta: %s", blackOutFrom, calendar.getTimeInMillis(), timeDeltaMillis));
        Assert.assertTrue(blackOutFrom >= calendar.getTimeInMillis() - timeDeltaMillis, "From time is invalid");
        Assert.assertTrue(blackOutFrom <= calendar.getTimeInMillis() + timeDeltaMillis, "From time is invalid");

        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        LOGGER.debug(String.format("Comparing NotificationMessage.BLACK_OUT_TO actual: %s expected: %s delta: %s", blackOutFrom, calendar.getTimeInMillis(), timeDeltaMillis));
        Assert.assertTrue(blackOutTo >= calendar.getTimeInMillis() - timeDeltaMillis, "To time is invalid");
        Assert.assertTrue(blackOutTo <= calendar.getTimeInMillis() + timeDeltaMillis, "To time is invalid");
    }

    public void ValidateSMSBlackOutTimeDayOverLapTest() {
        NotificationMessage notificationMessage1 = null;
        NotificationMessage notificationMessage = new NotificationMessage(UUID.randomUUID().toString(),
                "FIS", "07478761452","44", "Sample text ", 9);
        try {
            BlackOutTime blackOutTime = new BlackOutTime("19:00", "03:00");
            notificationMessage.getProperties().put(NotificationMessage.BLACK_OUT_PERIOD, blackOutTime);
            notificationMessage1 = notificationService.sendSMS(notificationMessage);
        } catch (NotificationMessageException notificationException) {
            Assert.fail();
        } catch (JMSException e) {
            Assert.fail();
        }

        final long timeDeltaMillis = 1000, // 1 second
                blackOutFrom = (Long) notificationMessage1.getProperties().get(NotificationMessage.BLACK_OUT_FROM),
                blackOutTo = (Long) notificationMessage1.getProperties().get(NotificationMessage.BLACK_OUT_TO);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 19);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        LOGGER.debug(String.format("Comparing NotificationMessage.BLACK_OUT_FROM actual: %s expected: %s delta: %s", blackOutFrom, calendar.getTimeInMillis(), timeDeltaMillis));
        Assert.assertTrue(blackOutFrom >= calendar.getTimeInMillis() - timeDeltaMillis, "From time is invalid");
        Assert.assertTrue(blackOutFrom <= calendar.getTimeInMillis() + timeDeltaMillis, "From time is invalid");

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 3);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        LOGGER.debug(String.format("Comparing NotificationMessage.BLACK_OUT_TO actual: %s expected: %s delta: %s", blackOutFrom, calendar.getTimeInMillis(), timeDeltaMillis));
        Assert.assertTrue(blackOutTo >= calendar.getTimeInMillis() - timeDeltaMillis, "To time is invalid");
        Assert.assertTrue(blackOutTo <= calendar.getTimeInMillis() + timeDeltaMillis, "To time is invalid");
    }

    public void SMSTextValidatorTest() {
        String errorCode = null;
        NotificationMessage notificationMessage = new NotificationMessage(UUID.randomUUID().toString(),
                "FIS", "07478761452", "44",null, 9);
        try {
            notificationService.sendSMS(notificationMessage);
        } catch (NotificationMessageException notificationException) {
            errorCode = notificationException.getErrorCode();
        } catch (JMSException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        Assert.assertEquals("notification.message.invalid", errorCode);
    }

    public void SMSTextLengthValidatorTest() {
        String errorCode = null;
        NotificationMessage notificationMessage = new NotificationMessage(UUID.randomUUID().toString(),
                "FIS", "07478761452", "44","01234567890123456789012345678901234567890123456789012345678901234567890123456789"
                + "0123456789012345678901234567890123456789012345678901234567890123456789", 9);
        try {
            notificationService.sendSMS(notificationMessage);
        } catch (NotificationMessageException notificationException) {
            errorCode = notificationException.getErrorCode();
        } catch (JMSException e) {
            Assert.fail();
        }
        Assert.assertEquals("notification.message.length", errorCode);
    }

    public void SMSSenderIDValidatorTest() {
        String errorCode = null;
        NotificationMessage notificationMessage = new NotificationMessage(UUID.randomUUID().toString(),
                "", "07478761452","44", "Sample Message", 9);
        try {
            notificationService.sendSMS(notificationMessage);
        } catch (NotificationMessageException notificationException) {
            errorCode = notificationException.getErrorCode();
        } catch (JMSException e) {
            Assert.fail();
        }
        Assert.assertEquals("notification.senderid.invalid", errorCode);
    }

    public void SMSPriorityValidatorTest() {
        String errorCode = null;
        NotificationMessage notificationMessage = new NotificationMessage(UUID.randomUUID().toString(),
                "FIS", "07478761452","44", "Sample Message", 19);
        try {
            notificationService.sendSMS(notificationMessage);
        } catch (NotificationMessageException notificationException) {
            errorCode = notificationException.getErrorCode();
        } catch (JMSException e) {
            Assert.fail();
        }
        Assert.assertEquals("notification.priority.range", errorCode);
    }

    public void SMSRecipientNumberValidatorTest() {
        String errorCode = null;
        NotificationMessage notificationMessage = new NotificationMessage(UUID.randomUUID().toString(),
                "FIS", "123232323A","44", "Sample Message", 1);
        try {
            notificationService.sendSMS(notificationMessage);
        } catch (NotificationMessageException notificationException) {
            errorCode = notificationException.getErrorCode();
        } catch (JMSException e) {
            Assert.fail();
        }
        Assert.assertEquals("notification.recipientnumber.invalid", errorCode);
    }

}
