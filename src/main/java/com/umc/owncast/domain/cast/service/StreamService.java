package com.umc.owncast.domain.cast.service;

import com.umc.owncast.domain.cast.entity.Cast;
import com.umc.owncast.domain.cast.repository.CastRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
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
@RequiredArgsConstructor
public class StreamService {
    private final CastRepository castRepository;
    private static final String AUDIO_FILES_PATH = "src/main/resources/stream-test/"; // 이후 수정

    public Object stream(String filename, HttpHeaders headers) throws IOException {
        Path filePath = Paths.get(AUDIO_FILES_PATH).resolve(filename).normalize();
        return streamResource(headers, filePath);
    }

    public Object stream(Long castId, HttpHeaders headers) throws IOException {
        Cast cast = castRepository.findById(castId).orElseThrow(() -> new IllegalArgumentException("castId가 잘못되었습니다"));
        Path filePath = Paths.get(AUDIO_FILES_PATH).resolve(cast.getFilePath()).normalize();
        return streamResource(headers, filePath);
    }

    @NotNull
    private ResponseEntity<Object> streamResource(HttpHeaders headers, Path filePath) throws IOException {
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