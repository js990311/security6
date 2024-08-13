package com.study.security6.security.authorization.method.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BoardAuthorization {
    String value() default "";
    String boardId() default "boardId";
    CrudMethod method() default CrudMethod.READ;
}
