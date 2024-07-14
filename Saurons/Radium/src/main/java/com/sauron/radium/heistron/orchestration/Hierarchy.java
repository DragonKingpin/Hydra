package com.sauron.radium.heistron.orchestration;

public enum Hierarchy {
    Master  ( "Master" ),
    Slave   ( "Slave" );

    private final String value;

    Hierarchy( String value ){
        this.value = value;
    }

    public String getName(){
        return this.value;
    }

    public static String queryName( Hierarchy hierarchy ) {
        return hierarchy.getName();
    }

    public static Hierarchy queryHierarchy( String sz ) {
        return Hierarchy.valueOf( sz );
    }

}
