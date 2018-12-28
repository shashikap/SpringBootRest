package com.bokks.micro.springbootrestapi.filters;

import com.bokks.micro.springbootrestapi.model.UserRoles;

import javax.ws.rs.NameBinding;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@NameBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Secured {
    //UserRoles[] value() default {};
}
