package com.pinecone.framework.system.prototype;

public interface Pinenut {
    default TypeIndex prototype() {
        return Prototype.typeid( this );
    }

    default String  prototypeName() {
        return this.className();
    }

    default boolean isPrototypeOf( TypeIndex that ){
        return this.prototype().equals( that );
    }

    default String className() {
        return this.getClass().getSimpleName();
    }

    default String toJSONString() {
        return String.format(
                "\"[object %s(0x%s)]\"",
                this.className() , Integer.toHexString( this.hashCode() )
        );
    }

}
