package com.fisglobal.xpress.jms;

import java.util.Map;

import javax.jms.Destination;

import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.context.IntegrationObjectSupport;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.jms.DefaultJmsHeaderMapper;
import org.springframework.integration.jms.JmsHeaderMapper;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.util.Assert;

public class JmsDestinationPollingSourceWithDynamicSelector extends IntegrationObjectSupport implements MessageSource<Object> {

	private final JmsTemplate jmsTemplate;

	private volatile Destination destination;

	private volatile String destinationName;

	private volatile MessageSelectorProvider messageSelectorProvider = new NullMessageSelectorProvider();

	private volatile JmsHeaderMapper headerMapper = new DefaultJmsHeaderMapper();


	public JmsDestinationPollingSourceWithDynamicSelector(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}


	public void setDestination(Destination destination) {
		Assert.isNull(this.destinationName, "The 'destination' and 'destinationName' properties are mutually exclusive.");
		this.destination = destination;
	}

	public void setDestinationName(String destinationName) {
		Assert.isNull(this.destination, "The 'destination' and 'destinationName' properties are mutually exclusive.");
		this.destinationName = destinationName;
	}

	@Override
	public String getComponentType() {
		return "jms:inbound-channel-adapter";
	}

	public void setHeaderMapper(JmsHeaderMapper headerMapper) {
		this.headerMapper = headerMapper;
	}

	/**
	 * Will receive a JMS {@link javax.jms.Message} converting and returning it as 
	 * a Spring Integration {@link Message}. This method will also use the current
	 * {@link JmsHeaderMapper} instance to map JMS properties to the MessageHeaders.
	 */
	@SuppressWarnings("unchecked")
	public Message<Object> receive() {
		Message<Object> convertedMessage = null;
		javax.jms.Message jmsMessage = this.doReceiveJmsMessage();
		if (jmsMessage == null) {
			return null;
		}
		try {
			// Map headers
			Map<String, Object> mappedHeaders = this.headerMapper.toHeaders(jmsMessage);
			MessageConverter converter = this.jmsTemplate.getMessageConverter();
			Object convertedObject = converter.fromMessage(jmsMessage);
			MessageBuilder<Object> builder = (convertedObject instanceof Message)
					? MessageBuilder.fromMessage((Message<Object>) convertedObject) : MessageBuilder.withPayload(convertedObject);
			convertedMessage = builder.copyHeadersIfAbsent(mappedHeaders).build();
		}
		catch (Exception e) {
			throw new MessagingException(e.getMessage(), e);
		}
		return convertedMessage;
	}

	private javax.jms.Message doReceiveJmsMessage() {
		javax.jms.Message jmsMessage = null;
		if (this.destination != null) {
			jmsMessage = this.jmsTemplate.receiveSelected(this.destination, messageSelectorProvider.getMessageSelector());
		}
		else if (this.destinationName != null) {
			jmsMessage = this.jmsTemplate.receiveSelected(this.destinationName, messageSelectorProvider.getMessageSelector());
		}
		else {
			jmsMessage = this.jmsTemplate.receiveSelected(messageSelectorProvider.getMessageSelector());
		}
		return jmsMessage;
	}

    public MessageSelectorProvider getMessageSelectorProvider() {
        return messageSelectorProvider;
    }

    public void setMessageSelectorProvider(MessageSelectorProvider messageSelectorProvider) {
        this.messageSelectorProvider = messageSelectorProvider;
    }

}
