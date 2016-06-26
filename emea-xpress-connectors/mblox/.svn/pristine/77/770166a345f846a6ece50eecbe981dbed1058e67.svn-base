package com.fisglobal.xpress.emea.mblox;

import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.SimpleMessageConverter;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.io.Serializable;

/**
 * @author e1050475
 */
public class NotificationMessageConverter extends SimpleMessageConverter implements org.springframework.jms.support.converter.MessageConverter {

    public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
        if (object instanceof NotificationMessage) {
            final ObjectMessage objectMessage = super.createMessageForSerializable((Serializable) object, session);
            NotificationMessage textMessage = (NotificationMessage) object;
            if (textMessage.getProperties().containsKey(NotificationMessage.BLACK_OUT_FROM)) {
                objectMessage.setLongProperty(NotificationMessage.BLACK_OUT_FROM, (Long) textMessage.getProperties().
                        get(NotificationMessage.BLACK_OUT_FROM));
            }
            if (textMessage.getProperties().containsKey(NotificationMessage.BLACK_OUT_TO)) {
                objectMessage.setLongProperty(NotificationMessage.BLACK_OUT_TO, (Long) textMessage.getProperties().
                        get(NotificationMessage.BLACK_OUT_TO));
            }
            return objectMessage;
        } else {
            return super.toMessage(object, session);
        }
    }

    protected ObjectMessage createMessageForSerializable(Serializable object, Session session) throws JMSException {
        return session.createObjectMessage(object);
    }
}
