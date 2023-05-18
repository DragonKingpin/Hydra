package Pinecone.Framework.Util.Summer.prototype;

import Pinecone.Framework.Util.Summer.http.HttpMethod;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RouterMapping {
    String url() default "";

    boolean relative() default true;

    HttpMethod[] method() default {};

    String[] params() default {};

    String[] headers() default {};

    String[] consumes() default {};

    String[] produces() default {};
}
