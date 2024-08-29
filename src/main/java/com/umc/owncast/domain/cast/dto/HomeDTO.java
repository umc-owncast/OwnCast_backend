package com.umc.owncast.domain.cast.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HomeDTO {
    private String nickName;
    private String subCategory;
    private List<String> keywords;
}
