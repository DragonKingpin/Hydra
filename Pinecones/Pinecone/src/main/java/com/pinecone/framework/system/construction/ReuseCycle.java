package com.pinecone.framework.system.construction;

public enum ReuseCycle {
    // Passively(Lazy) loading and instancing, and only have the one static singleton in the whole program scope.
    Singleton     ( "Singleton"     ),

    // Passively(Lazy) loading and instancing, and with instancing without pooling, free lifecycle.
    Disposable    ( "Disposable"    ),

    // Passively(Lazy) loading and instancing, and pooled the instanced object. Opt. Allocate / Free.
    Recyclable    ( "Recyclable"    ),

    // Positively loading and instancing if found, and only have the one static singleton in the whole program scope.
    PreSingleton  ( "PreSingleton"  ),

    // Positively loading and instancing if found, and pooled the instanced object. Opt. Allocate / Free.
    PreRecyclable ( "PreRecyclable" );

    private final String value;

    ReuseCycle( String value ){
        this.value = value;
    }

    public String getName(){
        return this.value;
    }

    public boolean isSingleton() {
        return this == ReuseCycle.Singleton || this == ReuseCycle.PreSingleton;
    }
}
