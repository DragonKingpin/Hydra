package com.walnut.sparta.ucdn.console.umc;

import com.pinecone.hydra.umct.stereotype.Iface;

@Iface
public interface RocketOperation {
    void taskComplete( String path );
}
