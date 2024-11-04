package com.example.imageapi.service;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
public class ImageService {

    @Autowired
    private final S3Client s3Client;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    @Value("${aws.s3.max-file-size}")
    private long MAX_FILE_SIZE;

    public ImageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadImage(String fileName, InputStream inputStream, long contentLength) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentLength(contentLength)
                    .contentType("image/jpeg") // 여기에 올바른 콘텐츠 타입을 설정
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, contentLength));
            return s3Client.utilities().getUrl(GetUrlRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build()).toString();
        } catch (S3Exception e) {
            // 에러 처리
            e.printStackTrace();
            return null;
        }
    }

    public List<String> listAllImages() {
        ListObjectsRequest listRequest = ListObjectsRequest.builder()
                .bucket(bucketName)
                .build();

        ListObjectsResponse listResponse = s3Client.listObjects(listRequest);

        return listResponse.contents().stream()
                .map(s3Object -> String.format("URL: %s, 파일명: %s, 업로드 시간: %s",
                        s3Client.utilities()
                                .getUrl(GetUrlRequest.builder().bucket(bucketName).key(s3Object.key()).build())
                                .toString(),
                        s3Object.key(),
                        s3Object.lastModified()))
                .collect(Collectors.toList());
    }

    public String deleteImage(String fileName) {
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        s3Client.deleteObject(deleteRequest);

        return String.format("{\"message\":\"%s 삭제 성공\"}", fileName);
    }
}
