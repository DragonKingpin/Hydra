package com.walnut.odin.proc.control;

import com.pinecone.framework.system.prototype.Pinenut;

public enum RemoteProcessControlFrameType implements Pinenut {
    ClientMuster( "CLIENT_MUSTER" ),
    ClientReady( "CLIENT_READY" ),
    FrameAck( "FRAME_ACK" ),
    ClientSnapshotBegin( "CLIENT_SNAPSHOT_BEGIN" ),
    ProcessMirror( "PROCESS_MIRROR" ),
    ClientSnapshotEnd( "CLIENT_SNAPSHOT_END" ),
    Apoptosis( "APOPTOSIS" ),
    Error( "ERROR" );

    protected String mszCode;

    RemoteProcessControlFrameType( String szCode ) {
        this.mszCode = szCode;
    }

    public String getCode() {
        return this.mszCode;
    }

    public static RemoteProcessControlFrameType parse( String szCode ) {
        for ( RemoteProcessControlFrameType type : RemoteProcessControlFrameType.values() ) {
            if ( type.getCode().equals( szCode ) ) {
                return type;
            }
        }
        throw new IllegalArgumentException( "Unsupported remote process control frame type: `" + szCode + "`." );
    }
}
