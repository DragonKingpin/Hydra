package com.pinecone.hydra.proc;

import java.util.Map;

import com.pinecone.framework.system.regime.Instrument;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.imperium.entity.EntityNode;

public class UniformProcessManager implements Instrument {

    protected Map<GUID, EntityNode> mEntityNodeMap;

}
