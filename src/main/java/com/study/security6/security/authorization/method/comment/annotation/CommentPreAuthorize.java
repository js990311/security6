package com.study.security6.security.authorization.method.comment.annotation;

import com.study.security6.security.authorization.method.CrudMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CommentPreAuthorize {
    String value() default "";
    String commentId() default "commentId";
    CrudMethod method() default CrudMethod.READ;
}
