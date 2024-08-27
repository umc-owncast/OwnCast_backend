package com.umc.owncast.domain.category.Initializer;

import com.umc.owncast.domain.category.entity.MainCategory;
import com.umc.owncast.domain.category.repository.MainCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MainCategoryInitializer implements CommandLineRunner {

    private final MainCategoryRepository mainCategoryRepository;

    @Override
    public void run(String... args) throws Exception {
        if (mainCategoryRepository.count() == 0) { // 데이터가 없는 경우만 추가
            mainCategoryRepository.save(new MainCategory(1L, "스포츠"));
            mainCategoryRepository.save(new MainCategory(2L, "음악"));
            mainCategoryRepository.save(new MainCategory(3L, "책"));
            mainCategoryRepository.save(new MainCategory(4L, "미술"));
            mainCategoryRepository.save(new MainCategory(5L, "드라마/영화"));
            mainCategoryRepository.save(new MainCategory(6L, "음식"));
            mainCategoryRepository.save(new MainCategory(7L, "시사/뉴스"));
            mainCategoryRepository.save(new MainCategory(8L, "직접 입력"));
        }
    }

}
