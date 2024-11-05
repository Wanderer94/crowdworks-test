# Image Upload API

## 프로젝트 구조

```plaintext
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.example.imageapi
│   │   │       ├── controller
│   │   │       │   └── ImageController.java  # API 요청 처리
│   │   │       └── service
│   │   │           └── ImageService.java     # S3 관련 비즈니스 로직
│   ├── test
│       └── java
│           └── com.example.imageapi
│               └── service
│                   └── ImageServiceTest.java  # API 단위 테스트
└── build.gradle
```
#### 실행 전 필수 설정
1. **AWS IAM 권한 설정**:
   - AWS 콘솔에서 IAM을 통해 S3 액세스 권한을 가진 사용자를 생성합니다. 생성된 사용자의 `access key`와 `secret key`는 프로젝트 실행 환경에 설정해 주어야 합니다.

2. **환경 변수 설정**:
   - 프로젝트의 `application.properties` 또는 `application.yml` 파일에 AWS 관련 설정을 추가합니다:
   ```properties
   aws.s3.bucket.name=your-s3-bucket-name
   aws.s3.max-file-size=5242880  # 최대 파일 크기: 5MB
   ```

## API 사용 방법

### 1. 이미지 업로드 API
 - 파일을 업로드하며, S3 버킷에 저장된 파일의 URL을 반환합니다.
   - 요청 파라미터:
     - `fileName` (필수): 저장할 파일명
     - `inputStream` (필수): 파일의 InputStream
     - `contentLength` (필수): 파일 크기
- **URL**: `/upload`
- **Method**: POST
- **Request Parameters**: `file` (multipart/form-data)
- **Request Example**:
  ```bash
  curl -X POST -F "file=@path/to/image.jpg" http://localhost:8080/api/upload
  ```
- **Response**: 이미지 URL

### 2. 이미지 조회 API
- S3 버킷에 저장된 모든 이미지 URL을 반환합니다.
- **URL**: `/list`
- **Method**: GET
- **Response**: 저장된 모든 이미지의 URL, 파일명, 업로드 시간을 포함한 리스트

### 3. 이미지 삭제 API
- 지정한 파일명을 S3 버킷에서 삭제합니다.
- **URL**: `/delete/{filename}`
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

# Image API

이 프로젝트는 AWS S3와 Spring Boot를 사용하여 이미지를 업로드하고 관리할 수 있는 API입니다.

### 환경 설정

#### AWS 설정

1. **IAM 권한 부여**
   - S3와 연결할 AWS IAM 사용자를 생성하고, `S3FullAccess` 권한을 부여합니다.
2. **환경 변수 설정**
   - 아래와 같이 `application.properties` 파일에 AWS S3 버킷명과 관련 설정을 추가하세요.

```properties
aws.s3.bucket.name=your-s3-bucket-name
aws.s3.max-file-size=5242880  # 최대 파일 크기: 5MB
```

3. **AWS SDK 버전 지정**
   - `build.gradle`의 `dependencies`에서 AWS SDK 버전을 지정합니다.

#### 빌드 및 실행 방법

1. **Gradle 빌드**
   ```bash
   ./gradlew build
   ```

2. **애플리케이션 실행**
   ```bash
   ./gradlew bootRun
   ```

#### Docker를 통한 실행
필요한 경우 Dockerfile을 생성해 Docker 컨테이너로 실행할 수 있습니다.

### 테스트 방법

1. **이미지 업로드 테스트**
   - JPEG, PNG, GIF 파일을 업로드하며, 파일 크기와 형식을 모두 검증합니다.

2. **이미지 리스트 조회 테스트**
   - S3 버킷에 업로드된 파일 목록을 반환하는지 확인합니다.

3. **이미지 삭제 테스트**
   - 특정 파일명을 S3에서 삭제하고 성공 메시지를 반환하는지 확인합니다.

#### 추가: 자동화 테스트 실행
테스트 코드는 `ImageServiceTest.java` 파일에 작성되어 있으며, `./gradlew test` 명령으로 실행할 수 있습니다.

#### API 테스트
API를 로컬에서 테스트하려면 다음 단계를 참조하세요. 각 엔드포인트는 [Postman](https://www.postman.com/) 또는 [curl](https://curl.se/)을 통해 테스트할 수 있습니다.

1. **이미지 업로드**:
   ```bash
   curl -X POST "http://localhost:8080/images/upload" \
   -F "file=@path_to_your_image_file" \
   -F "fileName=image-file-name.jpg"
   ```

2. **이미지 목록 조회**:
   ```bash
   curl -X GET "http://localhost:8080/images"
   ```

3. **이미지 삭제**:
   ```bash
   curl -X DELETE "http://localhost:8080/images/{fileName}"
   ```

```bash
./gradlew test
```

#### Notes

테스트 중 S3에 대한 실제 접근이 필요할 경우, AWS 자격 증명이 필요합니다. 로컬 환경에서 테스트하려면 `application.properties` 파일에 자격 증명 관련 정보를 설정합니다.
