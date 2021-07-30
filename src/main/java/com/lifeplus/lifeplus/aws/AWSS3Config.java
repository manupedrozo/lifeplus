package com.lifeplus.lifeplus.aws;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Manuel Pedrozo
 */
@Configuration
public class AWSS3Config implements ApplicationContextAware {

    @Value("${cloud.aws.region.static}")
    private String region;

    private ApplicationContext applicationContext;
    
    @Bean
    public AmazonS3 amazonS3Client() {
        String accessKey = applicationContext.getEnvironment().getProperty("accessKey", String.class);
        String secretKey = applicationContext.getEnvironment().getProperty("secretKey", String.class);

        if (accessKey == null || secretKey == null)
            return AmazonS3ClientBuilder.standard().withRegion(region).build();

        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(credentialsProvider)
                .withRegion(region)
                .build();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
