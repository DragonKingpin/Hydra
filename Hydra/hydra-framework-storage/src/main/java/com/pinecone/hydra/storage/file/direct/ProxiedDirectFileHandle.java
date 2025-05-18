package com.pinecone.hydra.storage.file.direct;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.ko.handle.KHandle;

public class ProxiedDirectFileHandle implements KHandle {
    protected String mszTreeNodeName;

    protected GUID   mTreeNodeGuid;

    protected DirectFileInstrument mDirectFileInstrument;

    public ProxiedDirectFileHandle( String treeNodeName, GUID treeNodeGuid, DirectFileInstrument directFileInstrument ) {
        this.mszTreeNodeName = treeNodeName;
        this.mTreeNodeGuid = treeNodeGuid;
        this.mDirectFileInstrument = directFileInstrument;
    }


    @Override
    public String getName() {
        return this.mszTreeNodeName;
    }

    @Override
    public GUID getGuid() {
        return this.mTreeNodeGuid;
    }
}
