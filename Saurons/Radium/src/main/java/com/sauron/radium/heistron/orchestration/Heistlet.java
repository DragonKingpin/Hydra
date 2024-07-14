package com.sauron.radium.heistron.orchestration;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Heistlet {
    String ValueKey = "value";

    String value() default "";
}
