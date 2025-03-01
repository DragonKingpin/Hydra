package com.walnut.sparta.ucdn.console.ufm;

import com.pinecone.hydra.umct.stereotype.Iface;
import com.walnut.sparta.ucdn.console.ufm.protocol.RequestHead;

@Iface ( "com.pinecone.hydra.uofs.ufm.FileMultiDistributionIface" )
public interface FileMultiDistributionIface {

    void startDistribution  ( RequestHead head, String path, long definitionSize );

    void setFrameMeta ( RequestHead head, UFMDClusterDO frameMeta );

    void transmitClusterFrame ( RequestHead head, UFMDClusterFrame contentVO );

    void frameTerminate( RequestHead head, String path, long segId );

}
