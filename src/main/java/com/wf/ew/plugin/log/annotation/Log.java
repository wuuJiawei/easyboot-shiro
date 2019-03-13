package com.wf.ew.plugin.log.annotation;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    /**
     * @author: 武佳伟丶 
     * @description: 当前操作的说明信息
     * @date: 19:58 2018/4/12 0012
     * @param: []
     * @return: java.lang.String
     */
    String value() default "";

}
