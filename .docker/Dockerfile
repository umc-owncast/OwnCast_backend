# Base 이미지 설정
FROM openjdk:17

# 빌드 아티팩트 설정
ARG JAR_FILE=build/libs/*.jar

# 환경 변수 설정
ENV AWS_ACCESS_KEY=${AWS_ACCESS_KEY} \
    AWS_SECRET_KEY=${AWS_SECRET_KEY} \
    GOOGLE_KEY=${GOOGLE_KEY} \
    JWT_KEY=${JWT_KEY} \
    NAVER_CLOUD_ID=${NAVER_CLOUD_ID} \
    OPENAI_KEY=${OPENAI_KEY} \
    RDS_PASSWORD=${RDS_PASSWORD} \
    RDS_URL=${RDS_URL} \
    RDS_USERNAME=${RDS_USERNAME} \
    DEFAULT_IMAGE_PATH=${DEFAULT_IMAGE_PATH} \
    S3_BUCKET=${S3_BUCKET}

# jar 파일 복사
COPY ${JAR_FILE} app.jar

# 애플리케이션 실행 명령어
ENTRYPOINT ["java", "-Dspring.profiles.active=${ACTIVE_PROFILE}", "-jar", "/app.jar"]
