package com.umc.owncast.domain.cast.service;

import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class StreamService {
    private static final String AUDIO_FILES_PATH = "src/main/resources/stream-test/"; // 이후 수정

    public Object stream(String filename, HttpHeaders headers) throws IOException {
        Path filePath = Paths.get(AUDIO_FILES_PATH).resolve(filename).normalize();
        UrlResource resource = new UrlResource(filePath.toUri());

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
