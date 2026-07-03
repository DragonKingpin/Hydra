package com.walnut.odin.formation;

public enum FormationPageStatus {
    Pending   ( "Pending" ),
    Claimed   ( "Claimed" ),
    Running   ( "Running" ),
    Completed ( "Completed" ),
    Blocked   ( "Blocked" ),
    Cancelled ( "Cancelled" );

    private final String mszName;

    FormationPageStatus( String name ) {
        this.mszName = name;
    }

    public String getName() {
        return this.mszName;
    }

    public static FormationPageStatus getByName( String name ) {
        for ( FormationPageStatus status : FormationPageStatus.values() ) {
            if ( status.getName().equals( name ) ) {
                return status;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.mszName;
    }
}
