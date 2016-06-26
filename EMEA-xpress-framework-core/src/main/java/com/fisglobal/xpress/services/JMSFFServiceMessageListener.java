package com.fisglobal.xpress.services;


import com.fnf.xes.framework.ServiceException;
import com.fnf.xes.framework.management.monitoring.EventManager;
import com.fnf.xes.framework.management.monitoring.MetricsPublisher;
import org.apache.log4j.Logger;

import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.LinkedList;
import java.util.List;

public class JMSFFServiceMessageListener implements MessageListener {

    private static final Logger LOGGER = Logger.getLogger(JMSFFServiceMessageListener.class);
    private static final String MESSAGE_METHOD = "JMS";
    private MetricsPublisher metPub;

    private List<JMSMessageService> services = new LinkedList<JMSMessageService>();

    public void onMessage(Message message) {
        LOGGER.debug("Sending message to Message dispatcher");
        LOGGER.debug("paylaod " + message);
        // Insure the Spring container is initialized prior to message processing

        metPub = MetricsPublisher.getInstance();
        metPub.setProperty(metPub.CHANNEL_NAME, MESSAGE_METHOD);
        metPub.setProperty(MetricsPublisher.SUCCESSFUL, Boolean.TRUE);
        try {
            for (JMSMessageService messageService : services) {
                if (messageService.accept(message)) {
                    messageService.processMessage(message);
                    break;
                }
            }
        } catch (ServiceException sExp) {
            LOGGER.error("Error in processing message ", sExp);
        } finally {
            EventManager em = EventManager.getInstance();
            em.processEvents();
        }
        LOGGER.debug("Message processed by BOC");
    }

    public List<JMSMessageService> getServices() {
        return services;
    }

    public void setServices(List<JMSMessageService> services) {
        this.services = services;
    }
}
