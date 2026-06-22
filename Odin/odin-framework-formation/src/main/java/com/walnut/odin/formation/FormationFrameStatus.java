package com.walnut.odin.formation;

public enum FormationFrameStatus {
    Pending        ( "Pending" ),
    Claimed        ( "Claimed" ),
    Submitted      ( "Submitted" ),
    DependencyWait ( "DependencyWait" ),
    Completed      ( "Completed" ),
    Failed         ( "Failed" ),
    Skipped        ( "Skipped" ),
    Cancelled      ( "Cancelled" );

    private final String mszName;

    FormationFrameStatus( String name ) {
        this.mszName = name;
    }

    public String getName() {
        return this.mszName;
    }

    public static FormationFrameStatus getByName( String name ) {
        for ( FormationFrameStatus status : FormationFrameStatus.values() ) {
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
