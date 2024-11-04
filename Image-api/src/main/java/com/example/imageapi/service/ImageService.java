package com.example.imageapi.service;

import com.example.imageapi.repository.ImageRepository;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;


    public String upLoadImage(InputStream inputStream, String fileName, long contentLength) {
        if (contentLength > 5 * 1024 * 1024) { // 예: 파일 크기 제한
            throw new IllegalArgumentException("File size exceeds limit.");
        }
        return imageRepository.upload(fileName, inputStream, contentLength); // Repository 호출
    }
}
