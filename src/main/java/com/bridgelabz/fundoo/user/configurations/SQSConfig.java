package com.bridgelabz.fundoo.user.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSClient;

@Configuration
public class SQSConfig {

	@Value("${queue.endpoint}")
	private String endpoint;

	@Value("${queue.name}")
	private String queueName;

	@Value("${awsAccessKeyId}")
	private String awsAccessKeyId;

	@Value("${awsSecretAccessKey}")
	private String awsSecretAccessKey;

	
	@Bean
	public AWSCredentials getAmazonClient() {
		AWSCredentials awsCredentials = null;
		try {
			awsCredentials = new AWSCredentials() {
				@Override
				public String getAWSSecretKey() {
					return awsSecretAccessKey;
				}

				@Override
				public String getAWSAccessKeyId() {
					return awsAccessKeyId;
				}
			};
		} catch (Exception exception) {
			throw new AmazonClientException("can not load your aws credentials, please check your credentials !!",
					exception);
		}
		return awsCredentials;
	}

}
