package com.walnut.odin.task;

import com.pinecone.framework.system.prototype.Pinenut;

public enum TaskResourceType implements Pinenut {

    CPUIntensive( "cpu", "CPUIntensive" ),
    GPUIntensive( "gpu", "GPUIntensive" ),
    MemoryIntensive( "memory", "MemoryIntensive" );

    private final String mszValue;
    private final String mszName;

    TaskResourceType( String szValue, String szName ) {
        this.mszValue = szValue;
        this.mszName  = szName;
    }

    public String getValue() {
        return this.mszValue;
    }

    public String getName() {
        return this.mszName;
    }

    public static TaskResourceType require( String szValue ) {
        for ( TaskResourceType type : TaskResourceType.values() ) {
            if ( type.getValue().equals( szValue ) ) {
                return type;
            }
        }

        throw new IllegalArgumentException( "Unsupported task resource type: " + szValue );
    }

}
