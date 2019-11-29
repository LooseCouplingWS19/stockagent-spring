package de.loosecoupling.assignment3.stockagent.messaging;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.loosecoupling.assignment3.stockagent.properties.AgentProperties;

@Component
public class StockConsumer implements MessageListener {

	private static final Logger LOG = LoggerFactory.getLogger(StockConsumer.class);
	
	@Autowired
	private AgentProperties agentProperties;

	@Override
	public void onMessage(Message message) {
		TextMessage mapMessage = (TextMessage) message;
		LOG.info("-----------------------------------------------------------------");
		LOG.info("Got a TextMessage to '" + agentProperties.getType() + "' Stocks");
		try {
			LOG.info("Message info: " + mapMessage.getText());
		} catch (JMSException e) {
			LOG.error(e.getMessage(), e);
		}
		
	}
}
