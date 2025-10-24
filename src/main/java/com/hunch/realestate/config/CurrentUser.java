package com.hunch.realestate.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 현재 로그인한 사용자를 파라미터로 주입받기 위한 어노테이션
 *
 * 사용 예시:
 * <pre>
 * public ResponseEntity<?> someMethod(@CurrentUser User user) {
 *     // user 객체를 바로 사용
 * }
 * </pre>
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUser {
}