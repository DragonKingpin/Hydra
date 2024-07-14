package com.pinecone.framework.util.json.hometype;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MapStructure {
    String value() default "";
}