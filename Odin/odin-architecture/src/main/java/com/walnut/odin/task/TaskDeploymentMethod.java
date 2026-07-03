package com.walnut.odin.task;

import com.pinecone.framework.system.prototype.Pinenut;

public enum TaskDeploymentMethod implements Pinenut {

    Direct( "direct" ),
    Authoritative( "authoritative" );

    private final String mszValue;

    TaskDeploymentMethod( String szValue ) {
        this.mszValue = szValue;
    }

    public String getValue() {
        return this.mszValue;
    }

    public static TaskDeploymentMethod parse( String szValue ) {
        if ( szValue == null || szValue.isEmpty() ) {
            return Direct;
        }

        for ( TaskDeploymentMethod method : TaskDeploymentMethod.values() ) {
            if ( method.getValue().equalsIgnoreCase( szValue ) || method.name().equalsIgnoreCase( szValue ) ) {
                return method;
            }
        }

        return Direct;
    }

    public static TaskDeploymentMethod require( String szValue ) {
        for ( TaskDeploymentMethod method : TaskDeploymentMethod.values() ) {
            if ( method.getValue().equalsIgnoreCase( szValue ) || method.name().equalsIgnoreCase( szValue ) ) {
                return method;
            }
        }

        throw new IllegalArgumentException( "Unsupported task deployment method: " + szValue );
    }

    public static boolean isDirect( String szValue ) {
        return TaskDeploymentMethod.parse( szValue ) == Direct;
    }

    public static boolean isAuthoritative( String szValue ) {
        return TaskDeploymentMethod.parse( szValue ) == Authoritative;
    }

}
