package com.example.imageapi.controller;

import com.example.imageapi.service.ImageService;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/images")
public class ImageController {
    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") InputStream inputStream, @RequestParam("name") String fileName)
            throws IOException {
        long contentLength = inputStream.available();
        return imageService.upLoadImage(inputStream,fileName,contentLength);
    }
}
