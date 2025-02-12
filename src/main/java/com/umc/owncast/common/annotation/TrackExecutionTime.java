package com.umc.owncast.common.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE}) // 메서드와 클래스에 부착 가능
@Retention(RetentionPolicy.RUNTIME) // 런타임에도 어노테이션 남아있도록 설정
public @interface TrackExecutionTime {
}
