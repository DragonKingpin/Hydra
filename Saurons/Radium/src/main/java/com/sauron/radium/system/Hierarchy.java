package com.sauron.radium.system;

import com.pinecone.hydra.system.HyHierarchy;

public enum Hierarchy implements HyHierarchy {
    H_MASTER  ( "Master" ),
    H_PALADIN ( "Paladin" ),
    H_MINION  ( "Minion" ),
    H_SLAVE   ( "Slave" );

    private final String value;

    Hierarchy( String value ){
        this.value = value;
    }

    @Override
    public String getName(){
        return this.value;
    }

    public static String queryName( Hierarchy hierarchy ) {
        return hierarchy.getName();
    }

    public static Hierarchy queryHierarchy( String sz ) {
        return Hierarchy.valueOf( "H_" + sz.toUpperCase() );
    }

    @Override
    public boolean isDominantClass() {
        return this == Hierarchy.H_MASTER || this == Hierarchy.H_PALADIN;
    }

    @Override
    public boolean isWorkerClass() {
        return this == Hierarchy.H_MINION || this == Hierarchy.H_SLAVE;
    }
}
