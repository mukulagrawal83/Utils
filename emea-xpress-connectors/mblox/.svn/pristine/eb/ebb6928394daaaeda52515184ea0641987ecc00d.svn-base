package com.fisglobal.xpress.emea.mblox;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * @author e1050475
 */
public class MbloxMessageListener implements MessageListener {
    private static final Logger LOGGER = Logger.getLogger(MbloxMessageListener.class);

    @Autowired
    private MbloxMessaging mbloxMessaging;

    private int messageFailure;
    private long sleepTimeAfterFailure = 2000;
    private int maxRetryMessage;


    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
    public void onMessage(Message message) {
        LOGGER.debug("Message failure count is" + messageFailure);
        try {
            LOGGER.debug("message ID is " + message.getJMSMessageID());
        } catch (JMSException jmsExp) {
            LOGGER.debug("Error in getting message ID");
        }
        if (messageFailure > 0 && messageFailure < 10) {
            sleepThread(getSleepTimeAfterFailure());

        } else if (messageFailure > 10) {
            sleepThread(getSleepTimeAfterFailure() + 5000);
        }
        if (message instanceof ObjectMessage) {
            NotificationMessage notificationMessage = null;
            try {
                notificationMessage = (NotificationMessage) ((ObjectMessage) message).getObject();
            } catch (JMSException jmsExp) {
                LOGGER.warn("Unable to cast message to notification message");
            }
            NotificationDetails notificationDetails = new NotificationDetails();
            notificationDetails.setSenderId(notificationMessage.getSenderId());
            notificationDetails.setMessage(notificationMessage.getMessage());
            notificationDetails.setSubscriberNumber(notificationMessage.getRecipientNumber());
            notificationDetails.setMessageId(notificationMessage.getMessageId());
            final NotificationStatus notificationStatus = mbloxMessaging.sendNotification(notificationDetails);
            if (notificationStatus.getMbloxConnectionStatus().getResponseStatusCode() != 200 && messageFailure < maxRetryMessage) {
                LOGGER.warn("Unable to deliver message to mBlox");
                messageFailure++;
                throw new NotificationMessageException(String.valueOf(notificationStatus.getMbloxConnectionStatus().getResponseStatusCode())
                        , "Unable to deliver message to mBlox");
            }else if(messageFailure ==maxRetryMessage && notificationStatus.getMbloxConnectionStatus().getResponseStatusCode() != 200){
                LOGGER.warn("Unable to deliver message to mBlox, even after 10 tries. Dumping message");
                LOGGER.warn("Message RecipientNumber " + notificationMessage.getRecipientNumber());
                LOGGER.warn("Message Priority        " + notificationMessage.getMessagePriority());
                LOGGER.warn("Message                 " + notificationMessage.getMessage());
            }
            messageFailure = 0;
            try {
                LOGGER.debug("Acknowledging message");
                message.acknowledge();
            } catch (JMSException jmsExp) {
                LOGGER.warn("Error in acknowledging the message");
            }
        }
    }


    public long getSleepTimeAfterFailure() {
        return this.sleepTimeAfterFailure;
    }

    /**
     * By default sleep time in 2s
     *
     * @param sleepTimeAfterFailure
     */
    public void setSleepTimeAfterFailure(long sleepTimeAfterFailure) {
        this.sleepTimeAfterFailure = sleepTimeAfterFailure;
    }

    private void sleepThread(long sleepTime) {
        try {
            LOGGER.debug("Thread sleeping due the failure is message processing");
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            LOGGER.error("Error in making thread sleep");
        }
    }

    public void setMaxRetryForMessage(int maxRetryForMessage) {
        this.maxRetryMessage = maxRetryForMessage;
    }
}
