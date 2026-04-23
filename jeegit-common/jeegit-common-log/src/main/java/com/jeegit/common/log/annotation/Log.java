package com.jeegit.common.log.annotation;

import com.jeegit.common.core.enums.BusinessTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {

    String title() default "";

    BusinessTypeEnum businessType() default BusinessTypeEnum.OTHER;

    boolean isSaveRequestData() default true;

    boolean isSaveResponseData() default true;
}
