package com.umc.owncast.common.initializer;

import com.umc.owncast.domain.category.entity.SubCategory;
import com.umc.owncast.domain.category.repository.SubCategoryRepository;
import com.umc.owncast.domain.enums.MainCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubCategoryInitializer implements CommandLineRunner {

    private final SubCategoryRepository subCategoryRepository;

    @Override
    public void run(String... args) throws Exception {
        if (subCategoryRepository.count() == 0) { // 데이터가 없는 경우만 추가
            // 스포츠
            subCategoryRepository.save(new SubCategory(1L, "축구", MainCategory.SPORTS, false));
            subCategoryRepository.save(new SubCategory(2L, "야구", MainCategory.SPORTS, false));
            subCategoryRepository.save(new SubCategory(3L, "농구", MainCategory.SPORTS, false));
            subCategoryRepository.save(new SubCategory(4L, "테니스", MainCategory.SPORTS, false));
            subCategoryRepository.save(new SubCategory(5L, "골프", MainCategory.SPORTS, false));
            subCategoryRepository.save(new SubCategory(6L, "수영", MainCategory.SPORTS, false));

            // 음악
            subCategoryRepository.save(new SubCategory(7L, "팝", MainCategory.MUSIC, false));
            subCategoryRepository.save(new SubCategory(8L, "재즈", MainCategory.MUSIC, false));
            subCategoryRepository.save(new SubCategory(9L, "힙합", MainCategory.MUSIC, false));
            subCategoryRepository.save(new SubCategory(10L, "클래식", MainCategory.MUSIC, false));
            subCategoryRepository.save(new SubCategory(11L, "K-POP", MainCategory.MUSIC, false));
            subCategoryRepository.save(new SubCategory(12L, "록", MainCategory.MUSIC, false));

            // 책
            subCategoryRepository.save(new SubCategory(13L, "소설", MainCategory.BOOK, false));
            subCategoryRepository.save(new SubCategory(14L, "자기개발", MainCategory.BOOK, false));
            subCategoryRepository.save(new SubCategory(15L, "판타지", MainCategory.BOOK, false));
            subCategoryRepository.save(new SubCategory(16L, "철학", MainCategory.BOOK, false));
            subCategoryRepository.save(new SubCategory(17L, "역사", MainCategory.BOOK, false));
            subCategoryRepository.save(new SubCategory(18L, "과학", MainCategory.BOOK, false));

            // 미술
            subCategoryRepository.save(new SubCategory(19L, "사진", MainCategory.ART, false));
            subCategoryRepository.save(new SubCategory(20L, "현대미술", MainCategory.ART, false));
            subCategoryRepository.save(new SubCategory(21L, "회화", MainCategory.ART, false));
            subCategoryRepository.save(new SubCategory(22L, "조각", MainCategory.ART, false));
            subCategoryRepository.save(new SubCategory(23L, "일러스트", MainCategory.ART, false));
            subCategoryRepository.save(new SubCategory(24L, "그래픽디자인", MainCategory.ART, false));

            // 드라마/영화
            subCategoryRepository.save(new SubCategory(25L, "로맨스", MainCategory.DRAMA, false));
            subCategoryRepository.save(new SubCategory(26L, "액션", MainCategory.DRAMA, false));
            subCategoryRepository.save(new SubCategory(27L, "코미디", MainCategory.DRAMA, false));
            subCategoryRepository.save(new SubCategory(28L, "공포", MainCategory.DRAMA, false));
            subCategoryRepository.save(new SubCategory(29L, "애니메이션", MainCategory.DRAMA, false));
            subCategoryRepository.save(new SubCategory(30L, "스릴러", MainCategory.DRAMA, false));

            // 음식
            subCategoryRepository.save(new SubCategory(31L, "한식", MainCategory.FOOD, false));
            subCategoryRepository.save(new SubCategory(32L, "양식", MainCategory.FOOD, false));
            subCategoryRepository.save(new SubCategory(33L, "중식", MainCategory.FOOD, false));
            subCategoryRepository.save(new SubCategory(34L, "일식", MainCategory.FOOD, false));
            subCategoryRepository.save(new SubCategory(35L, "베이커리", MainCategory.FOOD, false));
            subCategoryRepository.save(new SubCategory(36L, "채식", MainCategory.FOOD, false));

            // 시사/뉴스
            subCategoryRepository.save(new SubCategory(37L, "정치", MainCategory.NEWS, false));
            subCategoryRepository.save(new SubCategory(38L, "경제", MainCategory.NEWS, false));
            subCategoryRepository.save(new SubCategory(39L, "국제", MainCategory.NEWS, false));
            subCategoryRepository.save(new SubCategory(40L, "사회", MainCategory.NEWS, false));
            subCategoryRepository.save(new SubCategory(41L, "기술", MainCategory.NEWS, false));
            subCategoryRepository.save(new SubCategory(42L, "환경", MainCategory.NEWS, false));
        }
    }
}

