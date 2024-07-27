package com.umc.owncast.domain.cast.service;


import com.umc.owncast.common.response.ApiResponse;
import com.umc.owncast.domain.cast.dto.CastCreationRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CastService {
    private final ScriptService scriptService;
    // private final TTSService ttsService;
    // private final StreamService streamService;

    public ApiResponse<Object> createCast(CastCreationRequestDTO request){
        return null;
    }
}
