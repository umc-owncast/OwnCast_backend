package com.umc.owncast.domain.member.annotation;

import com.umc.owncast.domain.member.validation.ExistLoginIdValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExistLoginIdValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistLoginId {
    String message() default "이미 존재하는 아이디입니다";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
