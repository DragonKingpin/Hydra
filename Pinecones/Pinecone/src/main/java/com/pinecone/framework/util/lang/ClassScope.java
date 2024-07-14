package com.pinecone.framework.util.lang;

import com.pinecone.framework.system.prototype.Pinenut;

import java.util.List;

public interface ClassScope extends Pinenut {
    void           addScope           ( String szPackageName );

    void           addScope           ( ScopedPackage scope );

    void           removeScope        ( String szPackageName );

    void           removeScope        ( ScopedPackage scope );

    boolean        containsScope      ( String szPackageName );

    boolean        containsScope      ( ScopedPackage scope );

    ScopedPackage  getPackageByName   ( String szPackageName );

    List<ScopedPackage > getAllScopes ();

    List<String >  getAllNameScopes   ();
}
