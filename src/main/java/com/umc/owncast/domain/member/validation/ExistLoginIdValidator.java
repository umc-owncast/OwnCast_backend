package com.umc.owncast.domain.member.validation;

import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.domain.member.annotation.ExistLoginId;
import com.umc.owncast.domain.member.repository.MemberRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistLoginIdValidator implements ConstraintValidator<ExistLoginId, String> {

    private final MemberRepository memberRepository;

    @Override
    public void initialize(ExistLoginId constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean isValid = memberRepository.findByLoginId(value).isPresent();

        if (isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorCode.ID_ALREADY_EXIST.toString())
                    .addConstraintViolation();
        }

        return !isValid;
    }

}
