package Pinecone.Framework.System.Prototype;

public interface Pinenut {
    TypeIndex prototype();

    String  prototypeName();

    default boolean isPrototypeOf( TypeIndex that ){
        return this.prototype().equals( that );
    }

    default String toJSONString() {
        return "\"[object " + this.prototypeName() + "]\"";
    }

}
