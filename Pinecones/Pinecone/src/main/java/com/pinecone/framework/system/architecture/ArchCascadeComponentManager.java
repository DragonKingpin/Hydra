package com.pinecone.framework.system.architecture;

import com.pinecone.framework.unit.LinkedTreeMap;

import java.util.Collection;
import java.util.Map;

/**
 * The Omega Device
 * CascadeComponentManager can cascade control all node and its reference.
 * If one node owned its child, and others referred it:
 * 1. Mark-Sweep cascading effacement.
 * 1.1. Remove a node, and effaces itself and its own children will be erased from the whole scope.
 * 2. Cascading add.
 * 2.1 Add a new node, and will automatic marks and registers in its parent manager.
 * 3. Reference add.
 * 3.1 Refer a new node, and will only refers its instance without ownership.
 */
public abstract class ArchCascadeComponentManager extends ArchComponentManager implements CascadeComponentManager {
    private Map<String, Component >  mComponentListMap;

    protected ArchCascadeComponentManager( Map<String, Component > rootComponents, Map<String, Component > componentsList ) {
        super( rootComponents );

        this.mComponentListMap = componentsList;
    }

    protected ArchCascadeComponentManager() {
        super();

        this.mComponentListMap = new LinkedTreeMap<>();
    }

    protected Component onlyAdd( Component component ) {
        Component v = this.mComponentListMap.put( component.getFullName(), component );

        if( component instanceof CascadeComponent ) {
            if( ((CascadeComponent) component).parent() != null ) {
                return v;
            }
        }
        this.rootComponents().put( component.getFullName(), component );

        return v;
    }

    protected Component onlyRemove( String fullName ) {
        Component v = this.mComponentListMap.remove( fullName );

        if( v instanceof CascadeComponent ) {
            if( ((CascadeComponent) v).parent() != null ) {
                return v;
            }
        }
        this.rootComponents().remove( fullName );
        return v;
    }

    protected Map<String, Component > getComponentListMap() {
        return this.mComponentListMap;
    }


    @Override
    public void addComponent   ( Component component ) {
        this.onlyAdd( component );
    }

    @Override
    public void detachComponent( Component component ) {
        this.onlyRemove( component.getFullName() );
    }

    @Override
    public void removeComponent( String fullName ) {
        Component v = this.mComponentListMap.get( fullName );
        if( v != null ) {
            if( v instanceof CascadeComponent ){
                for( Component c : this.mComponentListMap.values() ) {
                    if( c instanceof CascadeComponent ) {
                        ((CascadeComponent) c).detachChildComponent( fullName );
                    }
                }
            }

            this.onlyRemove( fullName );
            if( v instanceof CascadeComponent ) {
                CascadeComponent component = (CascadeComponent) v;
                component.purge();
            }
        }
    }

    @Override
    public Component getComponentByFullName( String fullName ) {
        return this.mComponentListMap.get( fullName );
    }

    @Override
    public Component getRootComponentByFullName( String fullName ) {
        return super.getComponentByFullName( fullName );
    }

    @Override
    public Collection<Component> getComponentsRegisterList() {
        return this.mComponentListMap.values();
    }

    @Override
    public int componentScopeSize() {
        return this.getComponentListMap().size();
    }
}
