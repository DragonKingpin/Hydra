package com.walnut.odin.formation;

public enum FormationRunStatus {
    Prepared  ( "Prepared" ),
    Running   ( "Running" ),
    Suspended ( "Suspended" ),
    Completed ( "Completed" ),
    PartialCompleted ( "PartialCompleted" ),
    Failed    ( "Failed" ),
    Cancelled ( "Cancelled" );

    private final String mszName;

    FormationRunStatus( String name ) {
        this.mszName = name;
    }

    public String getName() {
        return this.mszName;
    }

    public static FormationRunStatus getByName( String name ) {
        for ( FormationRunStatus status : FormationRunStatus.values() ) {
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
