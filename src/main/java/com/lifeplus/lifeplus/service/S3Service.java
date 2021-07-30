package com.lifeplus.lifeplus.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @author Manuel Pedrozo
 */
@Service
public class S3Service {

    private AmazonS3 amazonS3;

    @Value("${app.awsServices.bucketName}")
    private String bucketName;

    @Autowired
    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public String uploadFile(String fileName, File file) {
        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file));
        return amazonS3.getUrl(bucketName, fileName).toString();
    }

    public S3Object downloadFile(String fileName) {
        return amazonS3.getObject(bucketName, fileName);
    }

    public void deleteFile(String fileName) {
        amazonS3.deleteObject(bucketName, fileName);
    }

    public String getUrl(String fileName) {
        return amazonS3.getUrl(bucketName, fileName).toString();
    }
}
