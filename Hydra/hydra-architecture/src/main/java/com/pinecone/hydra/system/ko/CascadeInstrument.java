package com.pinecone.hydra.system.ko;

import com.pinecone.framework.system.regimentation.UniformCascadeNodus;
import com.pinecone.framework.util.name.Namespace;

public interface CascadeInstrument extends KernelObjectInstrument, UniformCascadeNodus {
    String EmptySuperiorPathScope = "";

    @Override
    CascadeInstrument parent();

    void setParent( CascadeInstrument parent );

    @Override
    default boolean isRoot() {
        return this.parent() == null;
    }

    default CascadeInstrument root() {
        return (CascadeInstrument) UniformCascadeNodus.super.root();
    }

    @Override
    Namespace getTargetingName();

    @Override
    void setTargetingName( Namespace name );

    @Override
    default void setTargetingName( String name ) {
        UniformCascadeNodus.super.setTargetingName( name );
    }

    @Override
    default String getSimpleName() {
        return this.getTargetingName().getSimpleName();
    }

    @Override
    default String getFullName() {
        return this.getTargetingName().getFullName();
    }

    /**
     * Superior Path Scope (Specialized namespace)
     * 上级键空间（专门命名空间）
     * e.g. `/proc`/pid/name => Scope : `/proc`
     * @return ParentPathScope
     */
    String getSuperiorPathScope();

    void applySuperiorPathScope( String superiorPathScope );

}
