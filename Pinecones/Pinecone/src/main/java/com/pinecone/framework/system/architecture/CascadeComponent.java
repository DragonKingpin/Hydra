package com.pinecone.framework.system.architecture;

import com.pinecone.framework.util.name.Namespace;

import java.util.Collection;

public interface CascadeComponent extends Component {
    CascadeComponent parent();

    void setParent( CascadeComponent parent );

    default boolean isRoot() {
        return this.parent() == null;
    }

    default CascadeComponent root() {
        CascadeComponent p = this;
        CascadeComponent c = p;
        while ( p != null ) {
            c = p;
            p = p.parent();
        }

        return c;
    }

    Collection<Component > children();

    Namespace getName();

    void setName( Namespace name );

    void setName( String name );

    @Override
    default String getSimpleName() {
        return this.getName().getSimpleName();
    }

    @Override
    default String getFullName() {
        return this.getName().getFullName();
    }

    CascadeComponentManager getComponentManager();

    void addChildComponent      ( CascadeComponent child ) ;


    void detachChildComponent   ( String fullName );

    void referChildComponent    ( Component child ) ;

    void removeChildComponent   ( Component child );

    void removeChildComponent   ( String fullName ) ;

    default boolean ownedChild  ( CascadeComponent child ) {
        return child.parent() == this;
    }

    boolean hasOwnChild         ( CascadeComponent child ) ;

    boolean hasReferredChild    ( Component child ) ;

    Component getChildComponentByFullName( String fullName ) ;


    // Only clear all children reference.
    void clear() ;

    // if this has parent, mark it as null, and elevated to a root node.
    void independent( String newName );

    // Purge itself and its own children
    void purge();

    // Purge its own children
    void purgeChildren();

    int childSize() ;
}
