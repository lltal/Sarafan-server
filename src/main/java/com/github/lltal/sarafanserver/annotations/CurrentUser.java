package com.github.lltal.sarafanserver.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
@Target({ElementType.TYPE, ElementType.PARAMETER})
public @interface CurrentUser {
}
