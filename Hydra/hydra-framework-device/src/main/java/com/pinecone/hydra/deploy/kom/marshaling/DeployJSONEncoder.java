package com.pinecone.hydra.deploy.kom.marshaling;


import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.ElementNode;

public class DeployJSONEncoder implements DeployInstrumentEncoder {
    protected DeployInstrument instrument;

    public DeployJSONEncoder(DeployInstrument instrument ) {
        this.instrument = instrument;
    }

    @Override
    public Object encode( ElementNode node ) {
        return node.toJSONObject();
    }

}