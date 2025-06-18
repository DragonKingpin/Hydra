package com.walnut.odin.proc.server;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.umct.stereotype.Iface;

@Iface
public interface PMSMethodIface extends Pinenut {
    void start( String processId );
}
