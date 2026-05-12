package com.pinecone.hydra.device.kom.marshaling;


import com.pinecone.hydra.device.kom.DeviceInstrument;
import com.pinecone.hydra.device.kom.entity.ElementNode;

public class DeviceJSONEncoder implements DeviceInstrumentEncoder {
    protected DeviceInstrument instrument;

    public DeviceJSONEncoder(DeviceInstrument instrument ) {
        this.instrument = instrument;
    }

    @Override
    public Object encode( ElementNode node ) {
        return node.toJSONObject();
    }

}