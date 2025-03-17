package com.walnut.sailor.stream.fm;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umct.stereotype.Iface;
import com.walnut.sailor.stream.fm.protocol.RequestHead;

@Iface("com.walnut.sailor.stream.fm.FileMultiDistributionIface")
public interface FileMultiDistributionIface extends Pinenut {
    void startDistribution( RequestHead head, String fileName, String directionRouteToken );

    void transmitFileContent( RequestHead head, SFMFileFrame fileContent );

    void fileTransmitComplete( RequestHead head );
}
