package com.pinecone.hydra.system.ko.handle;

import com.pinecone.framework.util.id.GUID;

public interface ObjectTreeGUIDAddressingSectionHandle extends KHandle, SectionHandle {

    GUID queryGUIDByPath( String path );
}
