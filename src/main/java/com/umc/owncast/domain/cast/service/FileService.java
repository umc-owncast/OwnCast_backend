package com.umc.owncast.domain.cast.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.umc.owncast.common.exception.GeneralException;
import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.common.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFile(MultipartFile file) {
        if (file == null) {
            return null;
        }

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        String fileName = UUID.randomUUID().toString();
        try {
            amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);
        } catch (IOException e) {
            throw new GeneralException(ErrorCode._INTERNAL_SERVER_ERROR);
        }
        return "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + fileName;
    }

    public void deleteFile(String filePath) {
        if (StringUtil.isNullOrBlank(filePath)) {
            return;
        }
        try {
            amazonS3Client.deleteObject(bucket, filePath);
        } catch (Exception e) {
            System.out.println("FileService: IOException at stream() -> " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("S3 삭제에 실패하였습니다.");
        }
    }

    public ResponseEntity<UrlResource> streamFile(String filePath, HttpHeaders headers) throws IOException {
        UrlResource resource = new UrlResource(filePath);

        if (!resource.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        long fileLength = resource.contentLength();
        List<HttpRange> ranges = headers.getRange();
        HttpHeaders responseHeaders = new HttpHeaders();

        if (ranges.isEmpty()) {
            responseHeaders.setContentLength(fileLength);
            return ResponseEntity.ok()
                    .headers(responseHeaders)
                    .body(resource);
        } else {
            HttpRange range = ranges.get(0);
            long start = range.getRangeStart(fileLength);
            long end = range.getRangeEnd(fileLength);
            long contentLength = end - start + 1;

            responseHeaders.add("Content-Range", "bytes " + start + "-" + end + "/" + fileLength);
            responseHeaders.setContentLength(contentLength);

            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .headers(responseHeaders)
                    .body(resource);
        }
    }
}
