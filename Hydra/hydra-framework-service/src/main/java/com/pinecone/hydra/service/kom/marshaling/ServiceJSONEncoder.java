package com.pinecone.hydra.service.kom.marshaling;


import com.pinecone.hydra.service.kom.ServiceInstrument;
import com.pinecone.hydra.service.kom.entity.ElementNode;

public class ServiceJSONEncoder implements ServiceInstrumentEncoder {
    protected ServiceInstrument instrument;

    public ServiceJSONEncoder(ServiceInstrument instrument ) {
        this.instrument = instrument;
    }

    @Override
    public Object encode( ElementNode node ) {
        return node.toJSONObject();
    }

}