package com.umc.owncast.domain.cast.service;

import com.umc.owncast.domain.cast.dto.CastDTO;

public interface CastSaveService {
    Long saveCast(CastDTO.CastSaveRequestDTO castDTO);
}
