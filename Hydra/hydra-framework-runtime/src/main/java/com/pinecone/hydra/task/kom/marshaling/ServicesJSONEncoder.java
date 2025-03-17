package com.pinecone.hydra.task.kom.marshaling;


import com.pinecone.hydra.task.kom.ServiceInstrument;
import com.pinecone.hydra.task.kom.entity.ElementNode;

public class ServicesJSONEncoder implements ServicesInstrumentEncoder {
    protected ServiceInstrument instrument;

    public ServicesJSONEncoder( ServiceInstrument instrument ) {
        this.instrument = instrument;
    }

    @Override
    public Object encode( ElementNode node ) {
        return node.toJSONObject();
    }

}