package com.example.imageapi.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import software.amazon.awssdk.services.s3.S3Client;

@SpringBootTest
public class ImageServiceTest {

    @Autowired
    private ImageService imageService;

    @Autowired
    private S3Client s3Client;

    @Test
    public void testUploadImage() {
        String fileName = "test-image.jpg";
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes(StandardCharsets.UTF_8));
        long contentLength = 1024; // 예제용 데이터 크기

        String imageUrl = imageService.uploadImage(fileName, inputStream, contentLength);
        Assert.notNull(imageUrl, "이미지 URL은 null이 아닙니다.");
    }

    @Test
    public void testListAllImages() {
        List<String> images = imageService.listAllImages();
        Assert.notEmpty(images, "이미지 목록은 비어 있지 않습니다.");
    }

    @Test
    public void testDeleteImage() {
        String fileName = "test-image.jpg";
        String result = imageService.deleteImage(fileName);
        Assert.isTrue(result.contains("삭제 성공"), "삭제 성공 메시지를 포함해야 합니다.");
    }
}
