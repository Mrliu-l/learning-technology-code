package com.liu.framework.annotation;

import java.lang.annotation.*;

/**
 * @author liu_l
 * @Title: MrLiuController
 * @ProjectName workspace-idea
 * @Description: TODO
 * @date 2018/6/2611:11
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MrLiuController {
    String value() default "";
}
