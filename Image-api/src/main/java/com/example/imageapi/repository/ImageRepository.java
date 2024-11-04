package com.example.imageapi.repository;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class ImageRepository {

    private AmazonS3 amazonS3;

    @Autowired // 생성자 주입
    public ImageRepository(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    public String upload(String fileName, InputStream inputStream, long contentLength) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(contentLength);
        amazonS3.putObject(bucketName, fileName, inputStream, metadata);
        return amazonS3.getUrl(bucketName, fileName).toString();
    }
}
