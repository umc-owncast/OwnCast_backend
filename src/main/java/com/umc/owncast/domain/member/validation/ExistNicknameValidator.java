package com.umc.owncast.domain.member.validation;

import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.domain.member.annotation.ExistNickname;
import com.umc.owncast.domain.member.repository.MemberRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistNicknameValidator implements ConstraintValidator<ExistNickname, String> {

    private final MemberRepository memberRepository;

    @Override
    public void initialize(ExistNickname constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean isValid = memberRepository.findByNickname(value).isPresent();

        if (isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorCode.EXIST_NICKNAME.toString()).addConstraintViolation();
        }

        return !isValid;
    }
}
