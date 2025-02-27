package com.walnut.sparta.ucdn.console.domain.service;

import com.pinecone.framework.util.id.GUID;

public interface FileService {
    void remove(GUID fileGuid);
}
