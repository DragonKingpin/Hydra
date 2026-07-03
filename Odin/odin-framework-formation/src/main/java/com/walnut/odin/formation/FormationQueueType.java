package com.walnut.odin.formation;

public enum FormationQueueType {
    Ready          ( "Ready" ),
    FastLane       ( "FastLane" ),
    DependencyWait ( "DependencyWait" );

    private final String mszName;

    FormationQueueType( String name ) {
        this.mszName = name;
    }

    public String getName() {
        return this.mszName;
    }

    public static FormationQueueType getByName( String name ) {
        for ( FormationQueueType type : FormationQueueType.values() ) {
            if ( type.getName().equals( name ) ) {
                return type;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.mszName;
    }
}
