package com.walnut.sparta.ucdn.service.umct;

import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.umct.stereotype.Iface;

import java.io.IOException;

@Iface
public interface FileSyncDistribution {
    void fileDistribution(FileNode fileNode, String topic, String server, long startSegId, long endSegId ) throws IOException;
}
