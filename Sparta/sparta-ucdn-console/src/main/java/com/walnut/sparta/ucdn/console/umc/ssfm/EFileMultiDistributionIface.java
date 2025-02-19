package com.walnut.sparta.ucdn.console.umc.ssfm;

import com.pinecone.hydra.umct.stereotype.Iface;
import com.walnut.sparta.ucdn.console.infrastructure.EFileContent;
import com.walnut.sparta.ucdn.console.umc.ufm.protocol.RequestHead;

@Iface("com.pinecone.hydra.uofs.ufm.EFileMultiDistributionIface")
public interface EFileMultiDistributionIface {
    void startDistribution(RequestHead head, String fileName);

    void transmitFileContent(RequestHead head, EFileContent fileContent);
}
