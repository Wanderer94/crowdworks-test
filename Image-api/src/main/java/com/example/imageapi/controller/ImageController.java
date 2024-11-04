package com.example.imageapi.controller;

import com.example.imageapi.service.ImageService;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/images")
public class ImageController {
    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") InputStream inputStream, @RequestParam("name") String fileName)
            throws IOException {
        long contentLength = inputStream.available();
        return imageService.uploadImage(fileName, inputStream, contentLength);
    }

    @PostMapping("/delete")
    public String deleteImage(@RequestParam("fileName") String fileName) {
        imageService.deleteImage(fileName);
        return "Image deleted successfully.";
    }

    @GetMapping("/get")
    public String getImageUrl(@RequestParam("fileName") String fileName) {
        return imageService.getImageUrl(fileName);
    }
}
