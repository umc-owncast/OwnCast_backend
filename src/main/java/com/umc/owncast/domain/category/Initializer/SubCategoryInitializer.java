package com.umc.owncast.domain.category.Initializer;

import com.umc.owncast.domain.category.entity.MainCategory;
import com.umc.owncast.domain.category.entity.SubCategory;
import com.umc.owncast.domain.category.repository.MainCategoryRepository;
import com.umc.owncast.domain.category.repository.SubCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubCategoryInitializer implements CommandLineRunner {

    private final SubCategoryRepository subCategoryRepository;
    private final MainCategoryRepository mainCategoryRepository;

    @Override
    public void run(String... args) throws Exception {
        if (subCategoryRepository.count() == 0) { // 데이터가 없는 경우만 추가

            MainCategory sports = mainCategoryRepository.findById(1L).orElse(new MainCategory(1L, "스포츠"));
            MainCategory music = mainCategoryRepository.findById(2L).orElse(new MainCategory(2L, "음악"));
            MainCategory books = mainCategoryRepository.findById(3L).orElse(new MainCategory(3L, "책"));
            MainCategory arts = mainCategoryRepository.findById(4L).orElse(new MainCategory(4L, "미술"));
            MainCategory movie = mainCategoryRepository.findById(5L).orElse(new MainCategory(5L, "드라마/영화"));
            MainCategory foods = mainCategoryRepository.findById(6L).orElse(new MainCategory(6L, "음식"));
            MainCategory news = mainCategoryRepository.findById(7L).orElse(new MainCategory(7L, "시사/뉴스"));

            // 스포츠
            subCategoryRepository.save(new SubCategory(1L, "축구", sports, false));
            subCategoryRepository.save(new SubCategory(2L, "야구", sports, false));
            subCategoryRepository.save(new SubCategory(3L, "농구", sports, false));
            subCategoryRepository.save(new SubCategory(4L, "테니스", sports, false));
            subCategoryRepository.save(new SubCategory(5L, "골프", sports, false));
            subCategoryRepository.save(new SubCategory(6L, "수영", sports, false));

            // 음악
            subCategoryRepository.save(new SubCategory(7L, "팝", music, false));
            subCategoryRepository.save(new SubCategory(8L, "재즈", music, false));
            subCategoryRepository.save(new SubCategory(9L, "힙합", music, false));
            subCategoryRepository.save(new SubCategory(10L, "클래식", music, false));
            subCategoryRepository.save(new SubCategory(11L, "K-POP", music, false));
            subCategoryRepository.save(new SubCategory(12L, "록", music, false));

            // 책
            subCategoryRepository.save(new SubCategory(13L, "소설", books, false));
            subCategoryRepository.save(new SubCategory(14L, "자기개발", books, false));
            subCategoryRepository.save(new SubCategory(15L, "판타지", books, false));
            subCategoryRepository.save(new SubCategory(16L, "철학", books, false));
            subCategoryRepository.save(new SubCategory(17L, "역사", books, false));
            subCategoryRepository.save(new SubCategory(18L, "과학", books, false));

            // 미술
            subCategoryRepository.save(new SubCategory(19L, "사진", arts, false));
            subCategoryRepository.save(new SubCategory(20L, "현대미술", arts, false));
            subCategoryRepository.save(new SubCategory(21L, "회화", arts, false));
            subCategoryRepository.save(new SubCategory(22L, "조각", arts, false));
            subCategoryRepository.save(new SubCategory(23L, "일러스트", arts, false));
            subCategoryRepository.save(new SubCategory(24L, "그래픽디자인", arts, false));

            // 드라마/영화
            subCategoryRepository.save(new SubCategory(25L, "로맨스", movie, false));
            subCategoryRepository.save(new SubCategory(26L, "액션", movie, false));
            subCategoryRepository.save(new SubCategory(27L, "코미디", movie, false));
            subCategoryRepository.save(new SubCategory(28L, "공포", movie, false));
            subCategoryRepository.save(new SubCategory(29L, "애니메이션", movie, false));
            subCategoryRepository.save(new SubCategory(30L, "스릴러", movie, false));

            // 음식
            subCategoryRepository.save(new SubCategory(31L, "한식", foods, false));
            subCategoryRepository.save(new SubCategory(32L, "양식", foods, false));
            subCategoryRepository.save(new SubCategory(33L, "중식", foods, false));
            subCategoryRepository.save(new SubCategory(34L, "일식", foods, false));
            subCategoryRepository.save(new SubCategory(35L, "베이커리", foods, false));
            subCategoryRepository.save(new SubCategory(36L, "채식", foods, false));

            // 시사/뉴스
            subCategoryRepository.save(new SubCategory(37L, "정치", news, false));
            subCategoryRepository.save(new SubCategory(38L, "경제", news, false));
            subCategoryRepository.save(new SubCategory(39L, "국제", news, false));
            subCategoryRepository.save(new SubCategory(40L, "사회", news, false));
            subCategoryRepository.save(new SubCategory(41L, "기술", news, false));
            subCategoryRepository.save(new SubCategory(42L, "환경", news, false));
        }
    }
}

