package com.walnut.sparta.ucdn.console.domain.service;

import com.pinecone.framework.util.id.GUID;

public interface FileSystemService {
    void remove( GUID fileGuid );
}
