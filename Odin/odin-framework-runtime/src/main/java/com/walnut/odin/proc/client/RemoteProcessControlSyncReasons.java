package com.walnut.odin.proc.client;

import com.pinecone.framework.system.prototype.Pinenut;

public final class RemoteProcessControlSyncReasons implements Pinenut {

    public static final String Startup          = "startup";

    public static final String ChannelConnected = "channel-connected";

    public static final String ProcessMirrorRejected = "process-mirror-rejected";

    private RemoteProcessControlSyncReasons() {
    }
}
