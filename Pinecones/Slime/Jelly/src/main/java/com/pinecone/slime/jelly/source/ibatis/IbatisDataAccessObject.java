package com.pinecone.slime.jelly.source.ibatis;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IbatisDataAccessObject {
    String value() default "";

    // Which databases or data-manipulator that affinity to.
    // For multi databases scenario.
    String scope() default "";
}
