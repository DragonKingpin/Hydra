package com.pinecone.framework.system.prototype;

import java.nio.file.Path;
import java.util.List;

public interface FamilyContext extends Pinenut {
    List<Object > getGlobalScopes();

    FamilyContext setGlobalScopes( List<Object > globalScopes );

    Object parent();

    Object thisScope();

    Object root();

    Path[] getParentPaths();

    FamilyContext setParent( Object parent );

    FamilyContext setThisScope( Object thisScope );

    FamilyContext setRoot( Object root );

    FamilyContext setParentPaths( Path[] parentPaths );

    FamilyContext addParentPath( Path newPath );

    FamilyContext addGlobalScope( Object scope );

    default FamilyContext asProgenitor  ( Object root ) {
        this.setThisScope( root );
        this.setParent   ( root );
        this.setRoot     ( root );

        return this;
    }

    default boolean isFamilyAffinity ( FamilyContext otherContext ) {
        return this.root().equals( otherContext.root() );
    }

    default boolean isParentAffinity ( FamilyContext otherContext ) {
        return this.parent().equals( otherContext.parent() );
    }

}
