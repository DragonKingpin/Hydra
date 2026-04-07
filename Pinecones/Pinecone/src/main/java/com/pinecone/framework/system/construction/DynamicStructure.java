package com.pinecone.framework.system.construction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Repeatable;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DynamicStructure {
    String name() default "";

    String lookup() default "";

    // If true, will allowed this object member fields assignment likes the C/C++/Go struct.
    boolean directlyStruct() default false;

    Class<? > type() default Object.class;

    ReuseCycle cycle() default ReuseCycle.Singleton;

    DynamicStructure.AuthenticationType authenticationType() default DynamicStructure.AuthenticationType.CONTAINER;

    boolean shareable() default true;

    String mappedName() default "";

    String description() default "";

    // Instancing handle
    // TODO
    Class<?> provider() default void.class;

    String providerMethod() default "";

    enum AuthenticationType {
        CONTAINER,
        APPLICATION;

        AuthenticationType() {
        }
    }
}
