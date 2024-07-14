package com.sauron.radium.heistron;

public enum Metier {
    REAVER    ("Reaver"),
    STALKER   ("Stalker"),
    EMBEZZLER ("Embezzler");

    private final String value;
    Metier( String value ){
        this.value = value;
    }

    public String getName(){
        return this.value;
    }

    public static String queryName( Metier type ) {
        return type.getName();
    }

    public static Metier queryMetier( String sz ) {
        return Metier.valueOf( sz.toUpperCase() );
    }
}
