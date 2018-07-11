package com.liu.framework.annotation;

import java.lang.annotation.*;

/**
 * @author liu_l
 * @Title: MrLiuController
 * @ProjectName workspace-idea
 * @Description: TODO
 * @date 2018/6/2611:11
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MrLiuRequestParam {
    String value() default "";
}
