package com.pinecone.ulf.util.id;

public final class GUIDs {
    public static GUID64 GUID64( String s ) {
        return new GUID64( s );
    }

    public static GUID72 GUID72( String s ) {
        return new GUID72( s );
    }

    public static GUID72 Dummy72() {
        return new GUID72();
    }
}
