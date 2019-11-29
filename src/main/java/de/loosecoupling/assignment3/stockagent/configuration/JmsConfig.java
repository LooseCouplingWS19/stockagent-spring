package de.loosecoupling.assignment3.stockagent.configuration;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.loosecoupling.assignment3.stockagent.messaging.StockConsumer;
import de.loosecoupling.assignment3.stockagent.properties.AgentProperties;

@Configuration
public class JmsConfig {

	@Value("${spring.activemq.broker-url}")
	private String brokerUrl;
	@Value("${spring.activemq.user}")
	private String brokerUser;
	@Value("${spring.activemq.password}")
	private String brokerPassword;
	
	@Autowired
	private StockConsumer stockConsumer;
	@Autowired
	private AgentProperties agentProperties;

	@Bean
	public QueueConnectionFactory activeMQConnectionFactory() {
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
		activeMQConnectionFactory.setBrokerURL(brokerUrl);
		activeMQConnectionFactory.setUserName(brokerUser);
		activeMQConnectionFactory.setPassword(brokerPassword);

		return (QueueConnectionFactory) activeMQConnectionFactory;
	}
	
	@Bean
	public QueueConnection activeMQConnection(QueueConnectionFactory activeMQConnectionFactory) throws JMSException {
		QueueConnection activeMQConnection = activeMQConnectionFactory.createQueueConnection();
		activeMQConnection.start();
		return activeMQConnection;
	}
	
	@Bean
	public QueueSession activeMQSession(QueueConnection activeMQConnection) throws JMSException {
		return activeMQConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
	}
	
	@Bean
	public Queue agentQueue(QueueSession activeMQSession) throws JMSException {
		return activeMQSession.createQueue(agentProperties.getType());
	}
	
	@Bean
	public MessageConsumer topicSubscriber(QueueSession activeMQSession, Queue agentQueue) throws JMSException {
		MessageConsumer subscriber = activeMQSession.createConsumer(agentQueue);
		subscriber.setMessageListener(stockConsumer);
		return subscriber;
	}
	
}
