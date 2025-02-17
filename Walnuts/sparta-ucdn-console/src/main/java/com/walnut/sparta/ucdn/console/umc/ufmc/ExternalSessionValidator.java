package com.walnut.sparta.ucdn.console.umc.ufmc;

import com.pinecone.hydra.umct.stereotype.Iface;
import com.walnut.sparta.ucdn.console.umc.ufm.protocol.RequestHead;

@Iface
public interface ExternalSessionValidator {
    void fileTransmitComplete(RequestHead head);
}
