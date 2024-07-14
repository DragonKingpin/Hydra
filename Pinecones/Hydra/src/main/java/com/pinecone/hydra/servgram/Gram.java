package com.pinecone.hydra.servgram;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Gram {
    String ValueKey = "value";

    String value() default "";
}
