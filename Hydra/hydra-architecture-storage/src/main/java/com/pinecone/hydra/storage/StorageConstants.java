package com.pinecone.hydra.storage;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.ulf.util.guid.GUIDs;

public final class StorageConstants {
    public static final String PathSeparator            = "/";
    public static final String period                   = ".";
    public static final String StorageVersionSignature  = "Titan";
    public static final GUID             LocalhostGUID  = GUIDs.GUID128( "00000000-0000-0000-0000-000000000000" );

}
