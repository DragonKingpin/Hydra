package com.walnut.sailor.stream.fm;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umct.stereotype.Iface;
import com.walnut.sailor.stream.fm.protocol.RequestHead;

@Iface
public interface SessionValidator extends Pinenut {
    void fileTransmitComplete( RequestHead head );
}
