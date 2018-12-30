package com.bokks.micro.springbootrestapi.filters;

import com.bokks.micro.springbootrestapi.model.UserRoles;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Secured {
    UserRoles[] authorizedBy() default {};
}
