package com.devopsbuddy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
@EnableJpaRepositories(basePackages = "com.devopsbuddy.backend.persistence.repositories")
@EntityScan(basePackages="com.devopsbuddy.backend.persistence.domain.backend")
@EnableTransactionManagement
@PropertySource("file:///${user.home}/.devopsbuddy/application-common.properties")
public class ApplicationConfig {

	@Value("${aws.s3.profile}")
	private String awsProfileName;
	
	public AmazonS3Client s3ClientDep() {
		/*AWSCredentials credentials = new ProfileCredentialsProvider(awsProfileName).getCredentials();
		AmazonS3Client s3Client = new AmazonS3Client(credentials);
		Region region = Region.getRegion(Regions.EU_WEST_1);
		s3Client.setRegion(region);*/
		return null;
	}

	@Bean
	public AmazonS3 s3Client() {
		BasicAWSCredentials creds = new BasicAWSCredentials("AKIAJBOISJFLKMZUALPA","PfErRRkTfwqfaWEzOqKFD/M7k8twnDEmx7GmpPdR");
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds)).build();
		return s3Client;
	}

}