# Image Upload API

## 프로젝트 구조

```
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.example.imageapi
│   │   │       ├── controller       # REST 컨트롤러
│   │   │       ├── service          # 비즈니스 로직 및 S3 통신
│   │   │       ├── config           # AWS 클라이언트 설정
│   │   │       └── repository       # (생략 가능)
│   │   └── resources
│   │       ├── application.yml      # AWS S3 설정 포함
├── README.md
└── build.gradle
```

## API 사용 방법

### 1. 이미지 업로드 API

- **URL**: `/api/upload`
- **Method**: POST
- **Request Parameters**: `file` (multipart/form-data)
- **Request Example**:
  ```bash
  curl -X POST -F "file=@path/to/image.jpg" http://localhost:8080/api/upload
  ```
- **Response**: 이미지 URL

### 2. 이미지 조회 API

- **URL**: `/api/list`
- **Method**: GET
- **Response**: 저장된 모든 이미지의 URL, 파일명, 업로드 시간을 포함한 리스트

### 3. 이미지 삭제 API

- **URL**: `/api/delete/{filename}`
- **Method**: DELETE
- **Request Parameters**: `filename` (삭제할 파일 이름)
- **Request Example**:
  ```bash
  curl -X DELETE http://localhost:8080/api/delete/image.jpg
  ```
- **Response**: 삭제 성공 메시지

## AWS 설정

`src/main/resources/application.yml` 파일에 다음 설정을 추가하세요.

```yaml
aws:
  s3:
    bucket:
      name: <YOUR_BUCKET_NAME>
    region: <YOUR_REGION>
    accessKey: <YOUR_ACCESS_KEY>
    secretKey: <YOUR_SECRET_KEY>
```

## 빌드 및 실행 방법

```bash
./gradlew build
java -jar build/libs/imageapi-0.0.1-SNAPSHOT.jar
```

## 테스트 방법

### 1. 업로드 API 테스트
- 이미지 파일이 S3 버킷에 업로드되는지 확인합니다.
- 5MB 이상의 파일 업로드 시 에러가 발생하는지 확인합니다.

### 2. 조회 API 테스트
- S3 버킷의 모든 이미지 정보를 조회할 수 있는지 확인합니다.

### 3. 삭제 API 테스트
- 파일명을 기준으로 이미지를 S3 버킷에서 삭제할 수 있는지 확인합니다.
