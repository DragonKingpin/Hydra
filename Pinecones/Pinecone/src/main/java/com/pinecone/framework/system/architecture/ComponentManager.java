package com.pinecone.framework.system.architecture;

import com.pinecone.framework.system.prototype.Pinenut;

import java.util.Collection;

public interface ComponentManager extends Pinenut {
    void addComponent                ( Component component ) ;

    void detachComponent             ( Component component ) ;

    default void removeComponent     ( Component component ){
        this.removeComponent( component.getFullName() );
    }

    void removeComponent             ( String fullName );

    Component getComponentByFullName ( String fullName );

    int componentSize() ;

    Collection<Component > getComponents();
}
