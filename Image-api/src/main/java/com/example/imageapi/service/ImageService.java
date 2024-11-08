package com.example.imageapi.service;

import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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

    private static final Set<String> SUPPORTED_FILE_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif");


    public ImageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadImage(String fileName, InputStream inputStream, long contentLength) {

        validateFileSize(contentLength);
        String fileExtension = validateAndExtractFileExtension(fileName);

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentLength(contentLength)
                    .contentType("image/" + fileExtension) // 여기에 올바른 콘텐츠 타입을 설정
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, contentLength));
            return generateFileUrl(fileName);
        } catch (S3Exception e) {
            // 에러 처리
            e.printStackTrace();
            throw new RuntimeException("이미지 업로드 중 오류가 발생했습니다.");
        }
    }

    public List<String> listAllImages() {
        ListObjectsRequest listRequest = ListObjectsRequest.builder()
                .bucket(bucketName)
                .build();

        ListObjectsResponse listResponse = s3Client.listObjects(listRequest);

        return listResponse.contents().stream()
                .map(s3Object -> String.format("URL: %s, 파일명: %s, 업로드 시간: %s",
                        generateFileUrl(s3Object.key()),
                        s3Object.key(),
                        s3Object.lastModified()))
                .collect(Collectors.toList());
    }

    public String deleteImage(String fileName) {
        try {
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            s3Client.deleteObject(deleteRequest);
            return String.format("{\"message\":\"%s 삭제 성공\"}", fileName);
        } catch (S3Exception e) {
            // 삭제 실패에 대한 오류 처리
            e.printStackTrace();
            throw new RuntimeException("이미지 삭제 중 오류가 발생했습니다.");
        }
    }

    private void validateFileSize(long contentLength) {
        if (contentLength > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("파일 크기가 " + MAX_FILE_SIZE / (1024 * 1024) + "MB를 초과할 수 없습니다.");
        }
    }

    private String validateAndExtractFileExtension(String fileName) {
        String fileExtension = StringUtils.getFilenameExtension(fileName);
        if (fileExtension == null || !SUPPORTED_FILE_EXTENSIONS.contains(fileExtension.toLowerCase())) {
            throw new IllegalArgumentException("지원하지 않는 파일 형식입니다. JPEG, PNG, GIF만 허용됩니다.");
        }
        return fileExtension.toLowerCase();
    }

    private String generateFileUrl(String fileName) {
        return s3Client.utilities().getUrl(GetUrlRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build()).toString();
    }
}
