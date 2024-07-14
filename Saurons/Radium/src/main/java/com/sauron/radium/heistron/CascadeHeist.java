package com.sauron.radium.heistron;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.util.name.Namespace;
import com.pinecone.framework.util.name.UniNamespace;
import com.sauron.radium.heistron.orchestration.Hierarchy;

public interface CascadeHeist extends Heistum {
    String HeistNSSeparator = "::";

    CascadeHeist parent();

    default boolean isRoot() {
        return this.parent() == null;
    }

    default CascadeHeist root() {
        CascadeHeist p = this;
        CascadeHeist c = p;
        while ( p != null ) {
            c = p;
            p = p.parent();
        }

        return c;
    }

    Hierarchy getHierarchy();

    default boolean isMaster() {
        return this.getHierarchy() == Hierarchy.Master;
    }

    default boolean isSlave() {
        return this.getHierarchy() == Hierarchy.Slave;
    }

    Namespace getHeistNamespace();

    default String getInstanceFullName() {
        return this.getHeistNamespace().getFullName();
    }

    default String getInstanceName() {
        return this.getHeistNamespace().getName();
    }


    static Namespace newNamespace( String szSegmentName, @Nullable CascadeHeist parent ) {
        Namespace p = null;
        if( parent != null ) {
            p = parent.getHeistNamespace();
        }
        return new UniNamespace( szSegmentName, p, CascadeHeist.HeistNSSeparator );
    }
}
