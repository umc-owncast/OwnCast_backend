package com.umc.owncast.domain.cast.service;

import com.umc.owncast.domain.cast.repository.CastRepository;
import com.umc.owncast.domain.castcategory.repository.CastMainCategoryRepository;
import com.umc.owncast.domain.memberprefer.repository.MemberPreferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CastSearchServiceImpl {
    private final CastRepository castRepository;
    private final MemberPreferRepository memberPreferRepository;
    private final CastMainCategoryRepository castMainCategoryRepository;

//    public List<CastDTO.CastHomeDTO> getHomeCast(Integer page) {
//        // Long memberId = 토큰으로 정보 받아오기
//        Optional<MainPrefer> userMainCategory = memberPreferRepository.findById(1L);
//        //임시로 1L로 설정
//
//        List<CastDTO.CastHomeDTO> castHomeDTOList;
//
//        if (userMainCategory.isEmpty()) {
//            throw new UserHandler(ErrorCode.CAST_NOT_FOUND);
//        } else {
//            Long userCategoryId = userMainCategory.get().getId();
//            List<CastMainCategory> castMainCategories = castMainCategoryRepository.findTop5ByMainCategoryIdOrderByHitsDesc(userCategoryId, PageRequest.of(page - 1, 5));
//
//            castHomeDTOList = castMainCategories.stream().map(cast ->
//                    CastDTO.CastHomeDTO.builder()
//                            .id(cast.getCast().getId())
//                            .title(cast.getCast().getTitle())
//                            .memberName(cast.getCast().getMember().getUsername())
//                            .mainCategoryName(cast.getMainCategory().getName())
//                            .build()
//            ).toList();
//        }
//
//        return castHomeDTOList;
//    }
//
//    public List<CastDTO.CastHomeDTO> getCast(String keyword) {
//
//        List<Cast> castList = castRepository.castSearch(keyword);
//
//        return castList.stream().map(cast ->
//                CastDTO.CastHomeDTO.builder()
//                        .id(cast.getId())
//                        .title(cast.getTitle())
//                        //.memberName(cast.getMember().getUsername())
//                        //.mainCategoryName(castMainCategoryRepository.findByCastId(cast.getId()).getMainCategory().getName())
//                        .build()
//        ).toList();
//    }
}
