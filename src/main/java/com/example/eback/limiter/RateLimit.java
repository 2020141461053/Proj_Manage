package com.example.eback.limiter;


import com.alibaba.nacos.api.config.annotation.NacosValue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {

    // 默认每秒放入桶中的token
    @NacosValue(value = "${rateLimit:20}", autoRefreshed = true)
    int limitNum() default 20;

    String name() default "";
}
