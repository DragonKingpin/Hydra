package com.pinecone.hydra.storage.file.reparse;

import com.pinecone.framework.util.id.GUID;

public class ReparseLoopException extends ReparseException {
    public ReparseLoopException( GUID symbolicGuid, String path ) {
        super("UOFS symbolic reparse loop detected, symbolicGuid=" + symbolicGuid + ", path=" + path);
    }
}
