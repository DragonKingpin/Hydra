package com.walnuts.sparta.uofs.console.service;

import com.pinecone.framework.util.id.GUID;

public interface FileService {
    void remove(GUID fileGuid);
}
