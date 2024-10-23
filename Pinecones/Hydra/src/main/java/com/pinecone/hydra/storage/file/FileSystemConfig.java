package com.pinecone.hydra.storage.file;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.ko.KernelObjectConfig;
import com.pinecone.ulf.util.id.GUIDs;

public interface FileSystemConfig extends KernelObjectConfig {
    long defaultChunkSize = 10 * 1024 * 1024;
    GUID localhostGUID = GUIDs.GUID72("0000000-000000-0000-00");
    String filePrefix = "Titan";
}
