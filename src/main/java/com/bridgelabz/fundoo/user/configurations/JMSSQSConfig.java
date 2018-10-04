package com.bridgelabz.fundoo.user.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.bridgelabz.fundoo.user.listeners.SQSListener;

@Configuration
public class JMSSQSConfig {

	@Value("${queue.endpoint}")
	private String endpoint;

	@Value("${queue.name}")
	private String queueName;

	@Value("${awsAccessKeyId}")
	private String awsAccessKeyId;

	@Value("${awsSecretAccessKey}")
	private String awsSecretAccessKey;

	@Autowired
	private SQSListener sqsListener;

	@Bean
	public DefaultMessageListenerContainer jmsListenerContainer() {

		System.out.println("Listener config");
		@SuppressWarnings("deprecation")
		SQSConnectionFactory sqsConnectionFactory = SQSConnectionFactory.builder()
				.withAWSCredentialsProvider(new DefaultAWSCredentialsProviderChain()).withEndpoint(endpoint)
				.withAWSCredentialsProvider(awsCredentialsProvider).withNumberOfMessagesToPrefetch(10).build();
		System.out.println("sqslistener");

		DefaultMessageListenerContainer dmlc = new DefaultMessageListenerContainer();
		dmlc.setConnectionFactory(sqsConnectionFactory);
		dmlc.setDestinationName(queueName);
		dmlc.setMessageListener(sqsListener);

		return dmlc;
	}

	@Bean
	public JmsTemplate createJMSTemplate() {

		@SuppressWarnings("deprecation")
		SQSConnectionFactory sqsConnectionFactory = SQSConnectionFactory.builder()
				.withAWSCredentialsProvider(awsCredentialsProvider).withEndpoint(endpoint)
				.withNumberOfMessagesToPrefetch(10).build();

		JmsTemplate jmsTemplate = new JmsTemplate(sqsConnectionFactory);
		jmsTemplate.setDefaultDestinationName(queueName);
		jmsTemplate.setDeliveryPersistent(false);

		return jmsTemplate;

	}

	private final AWSCredentialsProvider awsCredentialsProvider = new AWSCredentialsProvider() {

		@Override
		public AWSCredentials getCredentials() {

			return new BasicAWSCredentials(awsAccessKeyId, awsSecretAccessKey);

		}

		@Override
		public void refresh() {

		}

	};
}
