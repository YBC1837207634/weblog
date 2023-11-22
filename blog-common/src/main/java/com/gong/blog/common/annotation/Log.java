package com.gong.blog.common.annotation;


import com.gong.blog.common.enums.BusinessType;
import com.gong.blog.common.enums.OperatorType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    /* 日志标题 */
    String title() default "";

    /*  业务类型 */
    BusinessType businessType() default BusinessType.OTHER;

    /* 操作类型 */
    OperatorType operatorType() default OperatorType.BACKSTAGE;

    boolean isParams() default true;

    boolean isResult() default true;

    boolean onlyError() default false;

}
