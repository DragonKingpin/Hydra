package com.walnut.sailor.stream.fm;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umct.stereotype.Iface;
import com.walnut.sparta.ucdn.console.infrastructure.EFileContent;
import com.walnut.sailor.stream.fm.protocol.RequestHead;

@Iface("com.pinecone.hydra.uofs.ufm.EFileMultiDistributionIface")
public interface EFileMultiDistributionIface extends Pinenut {
    void startDistribution( RequestHead head, String fileName );

    void transmitFileContent( RequestHead head, EFileContent fileContent );
}
